package com.example.musicapp.presintation.home

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.StatePlayer
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var durationLiveData: LiveData<Float>
    private lateinit var maxDurationLiveData: LiveData<Float>
    private lateinit var isPlay: LiveData<Boolean>
    private lateinit var currentPosition: LiveData<Int>
    private val isBound = MutableLiveData<Boolean>()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bottomPlayerAdapter: BottomPlayerAdapter
    private val viewModel by viewModel<HomeViewModel>()
    private var servicePlayer: PlayerService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()
        viewModel.setStatePlayer(StatePlayer.NONE)

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        setupViewPager(binding.bottomViewPager)
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
            override fun onPageScrollStateChanged(state: Int) {
                if (state == 0) {
                    val currentPosition = binding.bottomViewPager.currentItem
                    val lastPosition = viewModel.lastPosition

                    if (currentPosition != viewModel.lastPosition) {

                        if (lastPosition > currentPosition) {
                            viewModel.setStatePlayer(StatePlayer.PREVIOUS)
                        }
                        else {
                            viewModel.setStatePlayer(StatePlayer.NEXT)
                        }

                        viewModel.lastPosition = currentPosition
                    }
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

        viewModel.getMusicResult.observe(viewLifecycleOwner) { liveData->
            liveData.observe(viewLifecycleOwner) { array->
                viewModel.lastDownloadArray = array

                lifecycleScope.launch {
                    servicePlayer?.setMusicList(array)

                    bottomPlayerAdapter.setData(array)
                    binding.bottomViewPager.adapter = bottomPlayerAdapter

                    binding.bottomViewPager.doOnPreDraw {
                        binding.bottomViewPager.currentItem = currentPosition.value ?: 0
                    }
                }

                binding.viewPagerLayout.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE
            }
        }

        isBound.observe(viewLifecycleOwner) {
            if (it) {
                initSeekBar()

                if (isPlay.value == true) {
                    binding.mainPlayButton.isSelected = true
                    viewModel.isPlay = true
                    binding.lottieAnim.playAnimation()
                }
                else {
                    viewModel.isPlay = false
                    binding.lottieAnim.pauseAnimation()
                }
            }
        }
    }

    private fun initSeekBar() {
        maxDurationLiveData.observe(viewLifecycleOwner) {

        }

        durationLiveData.observe(viewLifecycleOwner) {

        }
    }

    override fun onDestroy() {
        requireActivity().unbindService(connectionToPlayerService)
        super.onDestroy()
    }

    @SuppressLint("ResourceType")
    private fun setupViewPager(viewPager: ViewPager2) {
        viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
        }

        val offsetPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_item_visible)
            .dpToPx(resources.displayMetrics)

        viewPager.setPadding(offsetPx, 0, offsetPx, 0)

        val pageMarginPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_current_item_horizontal_margin)
            .dpToPx(resources.displayMetrics)

        viewPager.setPageTransformer(
            MarginPageTransformer(pageMarginPx)
        )
    }

    private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int {
        return (this * displayMetrics.density).toInt()
    }

    private val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            maxDurationLiveData = binder.getMaxDuration()
            durationLiveData = binder.getCurrentDuration()
            currentPosition = binder.getCurrentPosition()
            isPlay = binder.isPlay()
            servicePlayer!!.setContext(view!!.context)
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }

    private fun pause() {
        binding.lottieAnim.pauseAnimation()
        binding.mainPlayButton.isSelected = false
        viewModel.isPlay = false
        servicePlayer?.setPlayerState(StatePlayer.PAUSE)
    }

    private fun play() {
        binding.lottieAnim.playAnimation()
        binding.mainPlayButton.isSelected = true
        viewModel.isPlay = true
        servicePlayer?.setPlayerState(StatePlayer.PLAY)
    }

    private fun previous() {
        servicePlayer?.setPlayerState(StatePlayer.PREVIOUS)
    }

    private fun next() {
        servicePlayer?.setPlayerState(StatePlayer.NEXT)
    }
}