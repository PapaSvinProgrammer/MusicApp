package com.example.musicapp.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.app.broadcastReceiver.NetworkReceiver
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.presentation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.app.service.player.DataPlayerType
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.app.service.player.PlayerInfo
import com.example.musicapp.app.service.player.TypeDataPlayer
import com.example.musicapp.domain.state.ControlPlayer
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var mediaController: MediaController
    private val viewModel by viewModel<MainViewModel>()

    private val bottomPlayerAdapter by lazy {
        BottomPlayerAdapter(
            navController = navController
        )
    }
    private val networkReceiver by lazy { NetworkReceiver() }

    @UnstableApi
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDarkMode()

        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerNetworkReceiver()
        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomViewPager.offscreenPageLimit = 1

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.bottomViewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_item_horizontal_margin
        )

        viewModel.getMusicResult.observe(this) { array->
            if (viewModel.countMusicList != 0) {
                return@observe
            }

            DataPlayerType.setType(TypeDataPlayer.GENERATE)
            viewModel.isFavorite(array.first().id ?: "")
            viewModel.setMediaItems(array)

            binding.progressIndicator.visibility = View.GONE
        }

        viewModel.startDownloadResult.observe(this) {
            if (viewModel.getMusicResult.value.isNullOrEmpty()) {
                binding.progressIndicator.visibility = View.VISIBLE
                MediaControllerManager.init(this)
                intiPermission()
            }
        }

        viewModel.getDarkModeResult.observe(this) {
            when (it){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        PlayerInfo.musicList.observe(this) { list ->
            bottomPlayerAdapter.setData(list)
        }

        MediaControllerManager.addInitCallback {
            if (it) {
                viewModel.isInitMediaController = true
                mediaController = MediaControllerManager.mediaController

                initMediaControllerListener()
                viewModel.getRandomMusic()
            }
        }

        binding.bottomViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()  {
            private var state = -1

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    this.state = ViewPager2.SCROLL_STATE_IDLE
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    //viewModel.setCurrentPosition(mediaController, position)
                }
            }
        })

        navController.addOnDestinationChangedListener{ _, destination, _->
            showOrHideBottomNavigation(destination)
        }

        networkReceiver.setCallback {
            drawNetworkError(it)
            viewModel.networkConnection = it
        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        MediaControllerManager.release()
        super.onDestroy()
    }

    private val mediaControllerListener = object: Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            PlayerInfo.setCurrentObject(MediaControllerManager.getCurrentMusic())
            val currentIndex = mediaController.currentMediaItemIndex

            if (binding.bottomViewPager.currentItem != currentIndex) {
                binding.bottomViewPager.setCurrentItem(currentIndex, false)
            }

            PlayerInfo.musicList.value?.let {
                val currentItem = mediaController.currentMediaItemIndex

                if (currentItem == it.size - 1) {
                    viewModel.putRandomMusic()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            PlayerInfo.setIsPlay(isPlaying)

            if (isPlaying) {
                initObserveToDuration()
            }
        }
    }

    private fun initObserveToDuration() {
        lifecycleScope.launch {
            MediaControllerManager.currentDuration().collect {
                PlayerInfo.setDuration(it)
            }
        }
    }

    private fun initMediaControllerListener() {
        binding.bottomViewPager.adapter = bottomPlayerAdapter
        mediaController.addListener(mediaControllerListener)

        bottomPlayerAdapter.setOnClickListener { controlPlayer, music ->
            when (controlPlayer) {
                ControlPlayer.LIKE -> viewModel.addMusicInSQLite(music)
                ControlPlayer.DISLIKE -> viewModel.deleteMusicFromSQLite(music.id ?: "")
                else -> {}
            }
        }
    }

    private fun showOrHideBottomNavigation(destination: NavDestination) {
        if (destination.id == R.id.playerFragment ||
            destination.id == R.id.loginFragment ||
            destination.id == R.id.registrationFragment ||
            destination.id == R.id.startFragment ||
            destination.id == R.id.settingPreferencesFragment ||
            destination.id == R.id.selectedListFragment)
        {
            binding.bottomNavigation.visibility = View.GONE
            binding.viewPagerLayout.visibility = View.GONE
        }
        else {
            viewModel.setStartState(true)
            binding.bottomNavigation.visibility = View.VISIBLE
            binding.viewPagerLayout.visibility = View.VISIBLE
            drawNetworkError(viewModel.networkConnection)
        }
    }

    @SuppressLint("InlinedApi")
    private fun intiPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )
    }

    private fun registerNetworkReceiver() {
        @Suppress("DEPRECATION")
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, intentFilter)
    }

    @Suppress("DEPRECATION")
    private fun drawNetworkError(state: Int?) {
        when (state) {
            ConnectivityManager.TYPE_WIFI -> binding.viewPagerLayout.visibility = View.VISIBLE
            ConnectivityManager.TYPE_MOBILE -> binding.viewPagerLayout.visibility = View.VISIBLE
            else -> binding.viewPagerLayout.visibility = View.GONE
        }
    }
}