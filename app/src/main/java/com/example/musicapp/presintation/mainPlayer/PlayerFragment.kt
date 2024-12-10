package com.example.musicapp.presintation.mainPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.state.ControlPlayer
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.state.SettingsPlayer
import com.example.musicapp.domain.player.state.StatePlayer
import com.example.musicapp.presintation.bottomSheet.PlayerBottomSheetDialog
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presintation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presintation.pagerAdapter.PlayerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val COUNT_MSEC_TO_RESET = 3000

class PlayerFragment: Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var arrayViewPager: ArrayList<Music>
    private val playerAdapter by lazy { PlayerAdapter() }
    private val viewModel by viewModel<PlayerViewModel>()
    private var argsPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = view.findNavController()

        HorizontalOffsetController().setPreviewOffsetMainPager(
            viewPager2 = binding.viewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible_main,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin_main
        )

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> playMusic()
                StatePlayer.PAUSE -> pauseMusic()
                StatePlayer.PREVIOUS -> previousMusic()
                StatePlayer.NEXT -> nextMusic()
                else -> {}
            }
        }

        viewModel.controlPlayer.observe(viewLifecycleOwner) {
            when (it) {
                ControlPlayer.LIKE -> executeLike()
                ControlPlayer.DISLIKE -> executeDislike()
                ControlPlayer.REPEAT -> executeRepeat()
                ControlPlayer.SHUFFLE -> executeShuffle()
                ControlPlayer.NOTE -> executeNote()
                else -> {}
            }
        }

        viewModel.missTimeResult.observe(viewLifecycleOwner) { time ->
            binding.missTime.text = time
        }

        viewModel.passTimeResult.observe(viewLifecycleOwner) { time ->
            binding.passTime.text = time
        }

        viewModel.getFavoriteMusicResult.observe(viewLifecycleOwner) {
            viewModel.isFavorite = it != null
            binding.likeView.isSelected = it != null
            viewModel.isDownloaded = it?.favoriteMusicEntity?.saveUri?.isNotEmpty() ?: false
        }

        binding.nextView.setOnClickListener {
            viewModel.setStatePlayer(StatePlayer.NEXT)
        }

        binding.previousView.setOnClickListener {
            viewModel.setStatePlayer(StatePlayer.PREVIOUS)
        }

        binding.playStopView.setOnClickListener {
            if (binding.playStopView.isSelected) {
                viewModel.setStatePlayer(StatePlayer.PAUSE)
            }
            else {
                viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }

        binding.shuffleView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.SHUFFLE)
        }

        binding.noteView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.NOTE)
        }

        binding.repeatView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.REPEAT)
        }

        binding.likeView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.LIKE)
        }

        binding.dislikeView.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.DISLIKE)
        }

        binding.shareButton.setOnClickListener {
            shareToOut()
        }

        binding.settingsButton.setOnClickListener {
            val bottomSheetDialog = PlayerBottomSheetDialog()

            val bundle = Bundle()
            bundle.putParcelable(PlayerBottomSheetDialog.CURRENT_MUSIC, arrayViewPager[binding.viewPager.currentItem])
            bundle.putBoolean(PlayerBottomSheetDialog.IS_FAVORITE, viewModel.isFavorite)
            bundle.putBoolean(PlayerBottomSheetDialog.IS_DOWNLOADED, viewModel.isDownloaded)
            bundle.putString(BottomPlayerAdapter.PARENT_ARG, arguments?.getString(BottomPlayerAdapter.PARENT_ARG))

            bottomSheetDialog.arguments = bundle
            initPlayerBottomSheet(bottomSheetDialog)

            requireActivity().supportFragmentManager.let {
                bottomSheetDialog.show(it, PlayerBottomSheetDialog.TAG)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset == 0f && position != viewModel.lastPosition) {
                    viewModel.servicePlayer.setCurrentPosition(position)
                    viewModel.lastPosition = position
                    changeNameAndGroupView()
                    resetControlPlayerUI()
                }
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekTo(seekBar?.progress)
                viewModel.getMissTime(seekBar?.progress)
            }
        })

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it) {
                initSeekBar()
                initServiceTools()
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()

        argsPosition = arguments?.getInt(BottomPlayerAdapter.POSITION_ARG)
        val array = arguments?.getParcelableArrayList(BottomPlayerAdapter.ARRAY_ARG, Music::class.java)
        val parent = arguments?.getString(BottomPlayerAdapter.PARENT_ARG)

        if (parent == BottomPlayerAdapter.PARENT_ARG_HOME) {
            binding.shuffleView.visibility = View.GONE
        }

        if (!viewModel.isCreated && array != null && argsPosition != null) {
            arrayViewPager = array

            val currentMusic = array[argsPosition ?: 0]
            viewModel.lastPosition = argsPosition ?: 0

            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.isCreated = true
                binding.viewPager.adapter = playerAdapter
                playerAdapter.setData(array)
                binding.viewPager.setCurrentItem(argsPosition ?: 0, false)
            }

            binding.groupTextView.isSelected = true
            binding.musicTextView.isSelected = true

            binding.groupTextView.text = currentMusic.group
            binding.musicTextView.text = currentMusic.name
        }

        viewModel.getMusicById(
            id = arrayViewPager[binding.viewPager.currentItem].id
        )
    }

    override fun onDestroy() {
        requireActivity().unbindService(viewModel.connectionToPlayerService)
        super.onDestroy()
    }

    private fun initPlayerBottomSheet(bottomSheetDialog: PlayerBottomSheetDialog) {
        bottomSheetDialog.settingsStateResult.observe(viewLifecycleOwner) {
            when (it) {
                SettingsPlayer.LIKE -> {
                    bottomSheetDialog.dialog?.hide()
                    executeLike()
                }

                SettingsPlayer.ADD_TO_PLAYLIST -> {}

                SettingsPlayer.SHARE -> {
                    shareToOut()
                }

                SettingsPlayer.MOVE_TO_GROUP -> {}
                SettingsPlayer.MOVE_TO_ALBUM -> {}
                SettingsPlayer.DELETE -> {}
                SettingsPlayer.DOWNLOAD -> {}
                SettingsPlayer.INFO -> {}

                SettingsPlayer.HATE -> {
                    bottomSheetDialog.dialog?.hide()
                    executeDislike()
                }

                SettingsPlayer.REPORT_PROBLEM -> {}
                else -> {}
            }
        }
    }

    private fun initSeekBar() {
        viewModel.maxDurationLiveData.observe(viewLifecycleOwner) {
            binding.seekBar.max = it
            viewModel.getMissTime(it)
            viewModel.getPassTime(it)
        }

        viewModel.durationLiveData.observe(viewLifecycleOwner) {
            binding.seekBar.progress = it
            viewModel.getMissTime(it)
            viewModel.getPassTime(it)
        }
    }

    private fun initServiceTools() {
        if (viewModel.isPlay.value == true) {
            binding.playStopView.isSelected = true
        }

        viewModel.currentPosition.observe(viewLifecycleOwner) { position ->
            binding.viewPager.setCurrentItem(position, false)

            viewModel.getMusicById(
                id = arrayViewPager[position].id
            )
        }
    }

    private fun resetControlPlayerUI() {
        binding.dislikeView.isSelected = false
        binding.likeView.isSelected = false

        binding.shuffleView.isSelected = false
        binding.repeatView.isSelected = false
        binding.noteView.isSelected = false

        binding.shuffleDot.visibility = View.GONE
        binding.noteDot.visibility = View.GONE
        binding.repeatDot.visibility = View.GONE
    }

    private fun executeNote() {
        when (binding.noteView.isSelected) {
            true -> {
                binding.noteView.isSelected = false
                binding.noteDot.visibility = View.GONE
            }

            false -> {
                binding.noteView.isSelected = true
                binding.noteDot.visibility = View.VISIBLE
            }
        }
    }

    private fun executeShuffle() {
        when (binding.shuffleView.isSelected) {
            true -> {
                binding.shuffleView.isSelected = false
                binding.shuffleDot.visibility = View.GONE
            }

            false -> {
                binding.shuffleView.isSelected = true
                binding.shuffleDot.visibility = View.VISIBLE
            }
        }
    }

    private fun executeRepeat() {
        when (binding.repeatView.isSelected) {
            true -> {
                binding.repeatView.isSelected = false
                binding.repeatDot.visibility = View.GONE
                viewModel.servicePlayer.repeat(false)
                binding.viewPager.isUserInputEnabled = true
            }

            false -> {
                binding.repeatView.isSelected = true
                binding.repeatDot.visibility = View.VISIBLE
                viewModel.servicePlayer.repeat(true)
                binding.viewPager.isUserInputEnabled = false
            }
        }
    }

    private fun executeDislike() {
        when (binding.dislikeView.isSelected) {
            true ->  binding.dislikeView.isSelected = false
            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в черный список",
                    Snackbar.LENGTH_SHORT
                ).show()

                resetControlPlayerUI()
                nextMusic()
            }
        }
    }

    private fun executeLike() {
        when (binding.likeView.isSelected) {
            true -> {
                viewModel.isFavorite = false
                binding.likeView.isSelected = false
                viewModel.deleteMusic(arrayViewPager[binding.viewPager.currentItem].id)
            }

            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в плей-лист \"Любимое\"",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.isFavorite = true
                binding.likeView.isSelected = true
                viewModel.addMusic(arrayViewPager[binding.viewPager.currentItem])
            }
        }
    }

    private fun nextMusic() {
        if (viewModel.isRepeat.value == true) {
            viewModel.servicePlayer.reset()
            return
        }

        binding.viewPager.currentItem += 1
        changeNameAndGroupView()
    }

    private fun previousMusic() {
        if (viewModel.isRepeat.value == true) {
            viewModel.servicePlayer.reset()
            return
        }

        if ((viewModel.durationLiveData.value ?: 0) > COUNT_MSEC_TO_RESET) {
            viewModel.servicePlayer.reset()
            return
        }

        binding.viewPager.currentItem -= 1
        changeNameAndGroupView()
    }

    private fun pauseMusic() {
        binding.playStopView.isSelected = false
        viewModel.servicePlayer.setPlayerState(StatePlayer.PAUSE)
    }

    private fun playMusic() {
        binding.playStopView.isSelected = true
        viewModel.servicePlayer.setPlayerState(StatePlayer.PLAY)
    }

    private fun shareToOut() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://")
            putExtra(Intent.EXTRA_TITLE, "Слушать в MusicApp")
            type = "text/plain"
        }

        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun changeNameAndGroupView() {
        val newObj = arrayViewPager[binding.viewPager.currentItem]

        binding.musicTextView.text = newObj.name
        binding.groupTextView.text = newObj.group
    }
}