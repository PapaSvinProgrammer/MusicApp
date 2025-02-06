package com.example.musicapp.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.app.broadcastReceiver.NetworkReceiver
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.presentation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import com.example.musicapp.app.service.audioDownloader.AudioDownloadManager
import com.example.musicapp.app.service.audioDownloader.AudioManager
import com.example.musicapp.app.service.player.module.DataPlayerType
import com.example.musicapp.app.service.player.module.PlayerInfo
import com.example.musicapp.app.service.player.module.TypeDataPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<MainViewModel>()
    private val bottomPlayerAdapter by lazy {
        BottomPlayerAdapter(
            navController = navController,
            viewModel = viewModel
        )
    }
    private val networkReceiver by lazy { NetworkReceiver() }

    @UnstableApi
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDarkMode()

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerNetworkReceiver()
        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomViewPager.adapter = bottomPlayerAdapter
        binding.bottomViewPager.offscreenPageLimit = 1

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.bottomViewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin
        )

        viewModel.getMusicResult.observe(this) { array->
            if (viewModel.countMusicList != 0) {
                return@observe
            }

            DataPlayerType.setType(TypeDataPlayer.GENERATE)
            viewModel.servicePlayer?.setMusicList(array)
            binding.progressIndicator.visibility = View.GONE

            viewModel.isFavorite(array.first().id ?: "")
        }

        viewModel.isBound.observe(this) {
            if (it) {
                initServiceTools()

                if (viewModel.musicList?.value == null && viewModel.startDownloadResult.value == true) {
                    viewModel.getRandomMusic()
                }
            }
        }

        viewModel.startDownloadResult.observe(this) {
            if (viewModel.getMusicResult.value.isNullOrEmpty()) {
                binding.progressIndicator.visibility = View.VISIBLE

                initDownloadManager()
                intiPermission()

                bindService(
                    Intent(this, PlayerService::class.java),
                    viewModel.connectionToPlayerService,
                    Context.BIND_AUTO_CREATE
                )

                viewModel.getRandomMusic()
            }
        }

        viewModel.getDarkModeResult.observe(this) {
            when (it){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.bottomViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback()  {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (positionOffset == 0f) {
                    viewModel.servicePlayer?.setCurrentPosition(position)
                }
            }
        })

        navController.addOnDestinationChangedListener{ _, destination, _->
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
                drawWifiError(viewModel.networkConnection)
            }

            if (destination.id == R.id.playerFragment ||
                destination.id == R.id.authorFragment ||
                destination.id == R.id.playlistItemFragment ||
                destination.id == R.id.albumFragment ||
                destination.id == R.id.playlistFavoriteFragment)
            {
                ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                    v.setPadding(0, 0, 0, 0)
                    insets
                }
            }
            else {
                ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(0, systemBars.top, 0, 0)
                    insets
                }
            }
        }

        networkReceiver.setCallback {
            drawWifiError(it)
            viewModel.networkConnection = it
        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        AudioManager.audioDownloadManager?.downloadCache?.release()
        AudioManager.audioDownloadManager?.downloadCache = null
        super.onDestroy()
    }

    private fun initServiceTools() {
        PlayerInfo.currentPosition.observe(this) { position ->
            if (position == viewModel.countMusicList - 1) {
                viewModel.addRandomMusic()
            }

            binding.bottomViewPager.doOnPreDraw {
                binding.bottomViewPager.setCurrentItem(position, false)
            }
        }

        PlayerInfo.currentObject.observe(this) {
            viewModel.isFavorite(it.id ?: "")
        }

        viewModel.musicList?.observe(this) { list ->
            bottomPlayerAdapter.setData(list)
            viewModel.countMusicList = list.size
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

    @UnstableApi
    private fun initDownloadManager() {
        AudioManager.audioDownloadManager = AudioDownloadManager(this)
    }

    private fun registerNetworkReceiver() {
        @Suppress("DEPRECATION")
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, intentFilter)
    }

    @Suppress("DEPRECATION")
    private fun drawWifiError(state: Int?) {
        when (state) {
            ConnectivityManager.TYPE_WIFI -> binding.viewPagerLayout.visibility = View.VISIBLE
            ConnectivityManager.TYPE_MOBILE -> binding.viewPagerLayout.visibility = View.VISIBLE
            else -> binding.viewPagerLayout.visibility = View.GONE
        }
    }
}