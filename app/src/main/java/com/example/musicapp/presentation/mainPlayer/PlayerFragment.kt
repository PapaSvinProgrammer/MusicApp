package com.example.musicapp.presentation.mainPlayer

import android.annotation.SuppressLint
import android.content.ComponentName
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.state.ControlPlayer
import com.example.musicapp.app.service.player.MediaService
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.bottomSheetMusicText.MusicTextBottomSheet
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.presentation.pagerAdapter.PlayerAdapter
import com.example.musicapp.app.service.video.VideoService
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment: Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var navController: NavController
    private lateinit var mediaController: MediaController
    private lateinit var controllerAsync: ListenableFuture<MediaController>
    private val playerAdapter by lazy { PlayerAdapter() }
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        setInitialPadding()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        binding.viewPager.adapter = playerAdapter

        initBlur()

        requireActivity().apply {
            bindService(
                Intent(this, VideoService::class.java),
                viewModel.connectionToVideoService,
                Context.BIND_AUTO_CREATE
            )
        }

        HorizontalOffsetController().setPreviewOffsetMainPager(
            viewPager2 = binding.viewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_player_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_item_player_horizontal_margin
        )

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> {
                    playMusic()
                    playVideo()
                }

                StatePlayer.PAUSE -> {
                    pauseMusic()
                    pauseVideo()
                }

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
            binding.likeButton.isSelected = it != null
        }

        viewModel.isBoundVideo.observe(viewLifecycleOwner) {

        }

        binding.nextButton.setOnClickListener {
            viewModel.setStatePlayer(StatePlayer.NEXT)
        }

        binding.previousButton.setOnClickListener {
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

        binding.shuffleButton.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.SHUFFLE)
        }

        binding.noteButton.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.NOTE)
        }

        binding.repeatButton.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.REPEAT)
        }

        binding.likeButton.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.LIKE)
        }

        binding.dislikeButton.setOnClickListener {
            viewModel.setControlPlayer(ControlPlayer.DISLIKE)
        }

        binding.shareButton.setOnClickListener {
            shareToOut()
        }

        binding.settingsButton.setOnClickListener {
            val bottomSheetDialog = MusicBottomSheet()

            val bundle = Bundle()


            bottomSheetDialog.arguments = bundle
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
//                if (positionOffset == 0f) {
//                    resetControlPlayerUI()
//
//                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
//                        viewModel.servicePlayer?.setCurrentPosition(position)
//                        resetControlPlayerUI()
//                    }
//                }
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
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()

        val sessionToken = SessionToken(
            requireContext(),
            ComponentName(requireContext(), MediaService::class.java)
        )

        controllerAsync = MediaController.Builder(requireContext(), sessionToken).buildAsync()
        controllerAsync.addListener({
            mediaController = controllerAsync.get()
        }, MoreExecutors.directExecutor())

    }

    override fun onDestroy() {
        MediaController.releaseFuture(controllerAsync)
        super.onDestroy()
    }

    private fun executeMoveToAuthor() {
        val bundle = Bundle()


        navController.popBackStack()
        navController.navigate(R.id.action_global_authorFragment, bundle)
    }

    private fun initSeekBar() {

    }

    private fun resetControlPlayerUI() {
        binding.dislikeButton.isSelected = false
        binding.likeButton.isSelected = false

        binding.shuffleButton.isSelected = false
        binding.repeatButton.isSelected = false
        binding.noteButton.isSelected = false

        binding.shuffleDot.visibility = View.GONE
        binding.noteDot.visibility = View.GONE
        binding.repeatDot.visibility = View.GONE
    }

    private fun executeNote() {
        val bottomSheetText = MusicTextBottomSheet()

        val bundle = Bundle()

        bottomSheetText.arguments = bundle
        requireActivity().supportFragmentManager.let {
            bottomSheetText.show(it, MusicTextBottomSheet.TAG)
        }
    }

    private fun executeShuffle() {
        when (binding.shuffleButton.isSelected) {
            true -> {
                binding.shuffleButton.isSelected = false
                binding.shuffleDot.visibility = View.GONE
            }

            false -> {
                binding.shuffleButton.isSelected = true
                binding.shuffleDot.visibility = View.VISIBLE
            }
        }
    }

    private fun executeRepeat() {
        when (binding.repeatButton.isSelected) {
            true -> {
                binding.repeatButton.isSelected = false
                binding.repeatDot.visibility = View.GONE

                binding.viewPager.isUserInputEnabled = true
            }

            false -> {
                binding.repeatButton.isSelected = true
                binding.repeatDot.visibility = View.VISIBLE

                binding.viewPager.isUserInputEnabled = false
            }
        }
    }

    private fun executeDislike() {
        when (binding.dislikeButton.isSelected) {
            true ->  binding.dislikeButton.isSelected = false
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
        when (binding.likeButton.isSelected) {
            true -> {
                viewModel.isFavorite = false
                binding.likeButton.isSelected = false
            }

            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в плей-лист \"Любимое\"",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.isFavorite = true
                binding.likeButton.isSelected = true
            }
        }
    }

    private fun nextMusic() {
        binding.seekBar.progress = 0

    }

    private fun previousMusic() {
        binding.seekBar.progress = 0
    }

    private fun pauseMusic() {
        binding.playStopView.isSelected = false
    }

    private fun pauseVideo() {
        if (viewModel.isSuccessVideo?.value == true) {
            viewModel.videoService?.pause()
            binding.videoPlayer.visibility = View.GONE
        }
    }

    private fun playMusic() {
        binding.playStopView.isSelected = true
    }

    private fun playVideo() {
        if (viewModel.isSuccessVideo?.value == true) {
            viewModel.videoService?.play()
            binding.videoPlayer.visibility = View.VISIBLE
        }
    }

    private fun resetVideo() {
        viewModel.videoService?.reset()
        binding.videoPlayer.visibility = View.GONE
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

    @SuppressLint("NewApi")
    private fun initBlur() {
        val blurRenderEffect = RenderEffect.createBlurEffect(500f, 500f, Shader.TileMode.CLAMP)
        binding.backImage.setRenderEffect(blurRenderEffect)
    }

    private fun setInitialPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }
}