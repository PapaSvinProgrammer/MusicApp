package com.example.musicapp.presentation.mainPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.ControlPlayer
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.state.SettingsPlayer
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.album.AlbumFragment
import com.example.musicapp.presentation.author.AuthorFragment
import com.example.musicapp.presentation.bottomSheet.MusicTextBottomSheet
import com.example.musicapp.presentation.bottomSheet.MusicBottomSheet
import com.example.musicapp.presentation.bottomSheet.MusicInfoBottomSheet
import com.example.musicapp.presentation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.presentation.pagerAdapter.PlayerAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val COUNT_MSEC_TO_RESET = 3000

class PlayerFragment: Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var navController: NavController
    private val playerAdapter by lazy { PlayerAdapter() }
    private val viewModel by viewModel<PlayerViewModel>()

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
        navController = view.findNavController()
        binding.viewPager.adapter = playerAdapter

        initBlur()

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
            viewModel.isDownloaded = it?.musicEntity?.saveUri?.isNotEmpty() ?: false
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
            val bottomSheetDialog = MusicBottomSheet()

            val bundle = Bundle()
            bundle.putParcelable(MusicBottomSheet.CURRENT_MUSIC, viewModel.currentObject?.value)
            bundle.putBoolean(MusicBottomSheet.IS_FAVORITE, viewModel.isFavorite)
            bundle.putBoolean(MusicBottomSheet.IS_DOWNLOADED, viewModel.isDownloaded)
            bundle.putString(BottomPlayerAdapter.PARENT_ARG, arguments?.getString(BottomPlayerAdapter.PARENT_ARG))

            bottomSheetDialog.arguments = bundle
            initPlayerBottomSheet(bottomSheetDialog)

            requireActivity().supportFragmentManager.let {
                bottomSheetDialog.show(it, MusicBottomSheet.TAG)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            var state = 0

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    this.state = state
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset == 0f) {
                    changeNameAndGroupView()
                    resetControlPlayerUI()

                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        viewModel.servicePlayer?.setCurrentPosition(position)
                        changeNameAndGroupView()
                        resetControlPlayerUI()
                    }
                }
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekTo(seekBar?.progress)
                viewModel.getMissTime(seekBar?.progress?.toLong())
            }
        })

        binding.musicLayout.setOnClickListener {
            executeMoveToAuthor()
        }

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

        val parent = arguments?.getString(BottomPlayerAdapter.PARENT_ARG)

        if (parent == BottomPlayerAdapter.PARENT_ARG_HOME) {
            binding.shuffleView.visibility = View.GONE
        }

        viewModel.getMusicById(
            id = viewModel.currentObject?.value?.id.toString()
        )
    }

    override fun onDestroy() {
        requireActivity().unbindService(viewModel.connectionToPlayerService)
        super.onDestroy()
    }

    private fun initPlayerBottomSheet(bottomSheetDialog: MusicBottomSheet) {
        bottomSheetDialog.settingsStateResult.observe(viewLifecycleOwner) {
            when (it) {
                SettingsPlayer.LIKE -> {
                    bottomSheetDialog.dismiss()
                    executeLike()
                }
                SettingsPlayer.ADD_TO_PLAYLIST -> {}
                SettingsPlayer.SHARE -> shareToOut()
                SettingsPlayer.MOVE_TO_GROUP -> {
                    bottomSheetDialog.dismiss()
                    executeMoveToAuthor()
                }
                SettingsPlayer.MOVE_TO_ALBUM -> {
                    bottomSheetDialog.dismiss()
                    executeMoveToAlbum()
                }
                SettingsPlayer.DELETE -> {}
                SettingsPlayer.DOWNLOAD -> {}
                SettingsPlayer.INFO -> musicInfo()
                SettingsPlayer.HATE -> executeDislike()
                SettingsPlayer.REPORT_PROBLEM -> {}
                SettingsPlayer.SHOW_MUSIC_TEXT -> executeNote()
                else -> {}
            }
        }
    }

    private fun musicInfo() {
        viewModel.getMusicInfo()

        val musicInfoBottomSheet = MusicInfoBottomSheet(
            getMusicInfoResult = viewModel.getMusicInfoResult
        )

        requireActivity().supportFragmentManager.let {
            musicInfoBottomSheet.show(it, MusicInfoBottomSheet.TAG)
        }
    }

    private fun executeMoveToAuthor() {
        val bundle = Bundle()
        val firebaseId = viewModel.currentObject?.value?.groupId
        bundle.putString(AuthorFragment.AUTHOR_KEY, firebaseId)

        navController.popBackStack()
        navController.navigate(R.id.action_global_authorFragment, bundle)
    }

    private fun executeMoveToAlbum() {
        val bundle = Bundle()
        val firebaseId = viewModel.currentObject?.value?.albumId
        bundle.putString(AlbumFragment.FIREBASE_KEY, firebaseId)

        navController.popBackStack()
        navController.navigate(R.id.action_global_albumFragment, bundle)
    }

    private fun initSeekBar() {
        viewModel.maxDurationLiveData?.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            viewModel.getMissTime(it)
            viewModel.getPassTime(it)
        }

        viewModel.durationLiveData?.observe(viewLifecycleOwner) {
            binding.seekBar.progress = it.toInt()
            viewModel.getMissTime(it)
            viewModel.getPassTime(it)
        }
    }

    private fun initServiceTools() {
        if (viewModel.isPlay?.value == true) {
            binding.playStopView.isSelected = true
        }

        viewModel.musicList?.observe(viewLifecycleOwner) { list ->
            playerAdapter.setData(list)
        }

        viewModel.currentPosition?.observe(viewLifecycleOwner) { position ->
            viewModel.getMusicById(
                id = viewModel.currentObject?.value?.id.toString()
            )

            val obj = viewModel.musicList?.value!![viewModel.currentPosition?.value ?: 0]

            Glide.with(binding.root)
                .load(obj.imageHigh)
                .into(binding.backImage)

            Glide.with(binding.root)
                .load(obj.imageGroup)
                .into(binding.groupImageView)

            binding.viewPager.doOnPreDraw {
                binding.viewPager.setCurrentItem(position, false)
            }
        }

        viewModel.bufferedPosition?.observe(viewLifecycleOwner) {
            binding.seekBar.secondaryProgress = it.toInt()
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
        viewModel.getMusicText()

        val bottomSheetText = MusicTextBottomSheet(viewModel.getMusicTextResult)

        requireActivity().supportFragmentManager.let {
            bottomSheetText.show(it, MusicTextBottomSheet.TAG)
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
                viewModel.servicePlayer?.repeat(false)
                binding.viewPager.isUserInputEnabled = true
            }

            false -> {
                binding.repeatView.isSelected = true
                binding.repeatDot.visibility = View.VISIBLE
                viewModel.servicePlayer?.repeat(true)
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
                viewModel.deleteMusic(viewModel.currentObject?.value?.id.toString())
            }

            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в плей-лист \"Любимое\"",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.isFavorite = true
                binding.likeView.isSelected = true
                viewModel.addFavoriteMusic(viewModel.currentObject?.value ?: Music())
            }
        }
    }

    private fun nextMusic() {
        if (viewModel.isRepeat?.value == true) {
            viewModel.servicePlayer?.reset()
            return
        }

        binding.viewPager.currentItem += 1
        changeNameAndGroupView()
    }

    private fun previousMusic() {
        if (viewModel.isRepeat?.value == true) {
            return
        }

        if ((viewModel.durationLiveData?.value ?: 0) > COUNT_MSEC_TO_RESET) {
            viewModel.servicePlayer?.reset()
            binding.seekBar.progress = 0
            return
        }

        binding.viewPager.currentItem -= 1
        changeNameAndGroupView()
    }

    private fun pauseMusic() {
        binding.playStopView.isSelected = false
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PAUSE)
    }

    private fun playMusic() {
        binding.playStopView.isSelected = true
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PLAY)
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
        val newObj = viewModel.currentObject?.value ?: Music()

        binding.musicTextView.text = newObj.name
        binding.groupTextView.text = newObj.group
    }

    @SuppressLint("NewApi")
    private fun initBlur() {
        val blurRenderEffect = RenderEffect.createBlurEffect(500f, 500f, Shader.TileMode.CLAMP)
        binding.backImage.setRenderEffect(blurRenderEffect)
    }
}