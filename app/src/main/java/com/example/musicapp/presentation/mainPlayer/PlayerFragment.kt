package com.example.musicapp.presentation.mainPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.media3.common.Player
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.state.ControlPlayer
import com.example.musicapp.app.service.player.PlayerInfo
import com.example.musicapp.app.service.video.VideoPlayer
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.presentation.bottomSheetMusicText.MusicTextBottomSheet
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheet
import com.example.musicapp.presentation.pagerAdapter.PlayerAdapter
import com.example.musicapp.app.service.video.VideoService
import com.example.musicapp.app.support.ConvertTime
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        viewModel.getFavoriteMusicResult.observe(viewLifecycleOwner) {
            viewModel.isFavorite = it != null
            binding.likeButton.isSelected = it != null
        }

        viewModel.isBoundVideo.observe(viewLifecycleOwner) {
            if (it) {
                initVideoService()
            }
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
            bundle.putParcelable(
                MusicBottomSheet.CURRENT_MUSIC,
                MediaControllerManager.getCurrentMusic()
            )

            bottomSheetDialog.arguments = bundle
            requireActivity().supportFragmentManager.let {
                bottomSheetDialog.show(it, MusicBottomSheet.TAG)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state != ViewPager2.SCROLL_STATE_IDLE) {
                    return
                }

                if (viewModel.statePlayer.value == StatePlayer.NEXT ||
                    viewModel.statePlayer.value == StatePlayer.PREVIOUS) {
                    viewModel.setStatePlayer(StatePlayer.NONE)
                    return
                }

                MediaControllerManager.setCurrentPosition(binding.viewPager.currentItem)
            }
        })

        binding.seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.seekTo(seekBar?.progress)
            }
        })

        binding.musicLayout.setOnClickListener {
            executeMoveToAuthor()
        }

        PlayerInfo.musicList.observe(viewLifecycleOwner) {
            playerAdapter.setData(it)
        }

        PlayerInfo.currentObject.observe(viewLifecycleOwner) {
            viewModel.getFavoriteMusic(it.id ?: "")
            updateCurrentDataUI(it)
            updateViewPagerUI()
            initSeekBar(it)
            resetVideo()
            setVideoClip(it)
        }

        PlayerInfo.duration.observe(viewLifecycleOwner) {
            binding.seekBar.progress = it.toInt()

            updateMissTime(
                max = MediaControllerManager.getCurrentMusic().time,
                progress = it.toInt()
            )
            updatePassTime(it.toInt())
        }

        PlayerInfo.isPlay.observe(viewLifecycleOwner) {
            binding.playStopView.isSelected = it
        }
    }

    override fun onDestroy() {
        requireActivity().apply {
            unbindService(viewModel.connectionToVideoService)
        }
        super.onDestroy()
    }

    private fun setVideoClip(music: Music) {
        viewModel.videoService?.setVideo(
            music = music
        )
    }

    private fun initVideoService() {
        binding.videoPlayer.player = VideoPlayer.exoPlayer
        viewModel.videoService?.setVideo(
            music = MediaControllerManager.getCurrentMusic()
        )

        viewModel.isSuccessVideo?.observe(viewLifecycleOwner) {
            if (it && PlayerInfo.isPlay.value == true) {
                binding.videoPlayer.visibility = View.VISIBLE
            }
        }
    }

    private fun updateViewPagerUI() {
        val currentIndex = MediaControllerManager.mediaController.currentMediaItemIndex

        if (currentIndex == binding.viewPager.currentItem) {
            return
        }

        viewModel.setStatePlayer(StatePlayer.NONE)
        binding.viewPager.setCurrentItem(currentIndex, false)
    }

    private fun executeMoveToAuthor() {
        val bundle = Bundle()

        navController.popBackStack()
        navController.navigate(R.id.action_global_authorFragment, bundle)
    }

    private fun initSeekBar(music: Music) {
        binding.seekBar.progress = 0
        binding.seekBar.max = music.time * 1000
    }

    private fun updateCurrentDataUI(it: Music) {
        binding.musicTextView.text = it.name
        binding.groupTextView.text = it.group

        Glide.with(binding.root)
            .load(it.imageGroup)
            .into(binding.groupImageView)

        Glide.with(binding.root)
            .load(it.imageLow)
            .into(binding.backImage)
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
                MediaControllerManager.mediaController.repeatMode = Player.REPEAT_MODE_ONE
            }

            false -> {
                binding.repeatButton.isSelected = true
                binding.repeatDot.visibility = View.VISIBLE

                binding.viewPager.isUserInputEnabled = false
                MediaControllerManager.mediaController.repeatMode = Player.REPEAT_MODE_OFF
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

                viewModel.deleteMusic(
                    id = MediaControllerManager.getCurrentMusic().id ?: ""
                )
            }

            false -> {
                Snackbar.make(
                    binding.root,
                    "Трек добавлен в плей-лист \"Любимое\"",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.isFavorite = true
                binding.likeButton.isSelected = true

                viewModel.addFavoriteMusic(
                    music = MediaControllerManager.getCurrentMusic()
                )
            }
        }
    }

    private fun nextMusic() {
        binding.viewPager.currentItem += 1
        MediaControllerManager.mediaController.seekToNext()
    }

    private fun previousMusic() {
        binding.viewPager.currentItem -= 1
        MediaControllerManager.mediaController.seekToPrevious()
    }

    private fun pauseMusic() {
        binding.playStopView.isSelected = false
        MediaControllerManager.mediaController.pause()
    }

    private fun pauseVideo() {
        if (viewModel.isSuccessVideo?.value == true) {
            viewModel.videoService?.pause()
            binding.videoPlayer.visibility = View.GONE
        }
    }

    private fun playMusic() {
        binding.playStopView.isSelected = true
        MediaControllerManager.mediaController.play()
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

    private fun updatePassTime(progress: Int) {
        binding.passTime.text = ConvertTime.convertInMinSec(progress / 1000)
    }

    private fun updateMissTime(max: Int, progress: Int) {
        val missTime = max * 1000 - progress
        binding.missTime.text = ConvertTime.convertInMinSec(missTime / 1000)
    }
}