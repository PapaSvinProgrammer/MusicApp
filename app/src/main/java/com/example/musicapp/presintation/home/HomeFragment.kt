package com.example.musicapp.presintation.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.state.StatePlayer
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presintation.pagerAdapter.HorizontalOffsetController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bottomPlayerAdapter: BottomPlayerAdapter
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = view.findNavController()
        viewModel.setStatePlayer(StatePlayer.NONE)

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.bottomViewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin
        )

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        bottomPlayerAdapter = BottomPlayerAdapter(navController, viewModel)

        binding.progressIndicator.visibility = View.VISIBLE
        if (viewModel.getMusicResult.value == null) {
            viewModel.getMusic()
        }

        binding.mainPlayButton.setOnClickListener {
            when (binding.mainPlayButton.isSelected) {
                true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                false -> viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.bottomViewPager.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            private var state1 = 0

            @SuppressLint("SwitchIntDef")
            override fun onPageScrollStateChanged(state: Int) {
                if (state == SCROLL_STATE_DRAGGING) state1 = SCROLL_STATE_DRAGGING

                if (state == SCROLL_STATE_IDLE && state1 == SCROLL_STATE_DRAGGING) {
                    if (binding.bottomViewPager.currentItem > viewModel.lastPosition) {
                        viewModel.setStatePlayer(StatePlayer.NEXT)
                    }
                    else {
                        viewModel.setStatePlayer(StatePlayer.PREVIOUS)
                    }

                    viewModel.lastPosition = binding.bottomViewPager.currentItem
                    state1 = 0
                }
            }
        })

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> play()
                StatePlayer.PAUSE -> pause()
                StatePlayer.PREVIOUS -> previous()
                StatePlayer.NEXT -> next()
                StatePlayer.NONE -> {}
            }
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { array->
            viewModel.lastDownloadArray.addAll(array)

            lifecycleScope.launch {
                viewModel.servicePlayer?.setMusicList(array)

                bottomPlayerAdapter.setData(array)
                binding.bottomViewPager.adapter = bottomPlayerAdapter

                binding.bottomViewPager.doOnPreDraw {
                    binding.bottomViewPager.currentItem = viewModel.currentPosition.value ?: 0
                }
            }

            binding.progressIndicator.visibility = View.GONE
        }

        viewModel.isBound.observe(viewLifecycleOwner) {
            if (it) {
                initSeekBar()

                viewModel.currentPosition.observe(viewLifecycleOwner) { position ->
                    binding.bottomViewPager.currentItem = position
                }

                viewModel.isPlayService.observe(viewLifecycleOwner) { state ->
                    if (state) {
                        binding.mainPlayButton.isSelected = true
                        binding.lottieAnim.playAnimation()
                    }
                    else {
                        binding.lottieAnim.pauseAnimation()
                    }
                }
            }
        }
    }

    private fun initSeekBar() {
        viewModel.maxDurationLiveData.observe(viewLifecycleOwner) {

        }

        viewModel.durationLiveData.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroy() {
        requireActivity().unbindService(viewModel.connectionToPlayerService)
        super.onDestroy()
    }

    private fun pause() {
        binding.lottieAnim.pauseAnimation()
        binding.mainPlayButton.isSelected = false
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PAUSE)
    }

    private fun play() {
        binding.lottieAnim.playAnimation()
        binding.mainPlayButton.isSelected = true
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PLAY)
    }

    private fun previous() {
        viewModel.servicePlayer?.setPlayerState(StatePlayer.PREVIOUS)
    }

    private fun next() {
        viewModel.servicePlayer?.setPlayerState(StatePlayer.NEXT)
    }
}