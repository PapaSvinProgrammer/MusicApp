package com.example.musicapp.presentation.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.presentation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presentation.pagerAdapter.HorizontalOffsetController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomPlayerAdapter: BottomPlayerAdapter
    private val viewModel by viewModel<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        bottomPlayerAdapter = BottomPlayerAdapter(
            navController = navController,
            viewModel = viewModel
        )
        binding.bottomViewPager.adapter = bottomPlayerAdapter

        bindService(
            Intent(this, PlayerService::class.java),
            viewModel.connectionToPlayerService,
            Context.BIND_AUTO_CREATE
        )

        HorizontalOffsetController().setPreviewOffsetBottomPager(
            viewPager2 = binding.bottomViewPager,
            nextItemVisibleSize = R.dimen.viewpager_item_visible,
            currentItemHorizontalMargin = R.dimen.viewpager_current_item_horizontal_margin
        )

        viewModel.getDarkMode()
        if (viewModel.darkModeResult) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        viewModel.getMusicResult.observe(this) { array->
            viewModel.servicePlayer?.setMusicList(array)
            binding.progressIndicator.visibility = View.GONE
        }

        viewModel.isBound.observe(this) {
            if (it) {
                initServiceTools()
            }
        }

        viewModel.startDownloadResult.observe(this) {
            if (viewModel.getMusicResult.value.isNullOrEmpty()) {
                binding.progressIndicator.visibility = View.VISIBLE
                viewModel.getMusic()
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
                destination.id == R.id.settingPreferencesFragment)
            {
                binding.bottomNavigation.visibility = View.GONE
                binding.viewPagerLayout.visibility = View.GONE
            }
            else {
                viewModel.setStartState(true)
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.viewPagerLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun initServiceTools() {
        viewModel.currentPosition?.observe(this) { position->
            binding.bottomViewPager.doOnPreDraw {
                binding.bottomViewPager.setCurrentItem(position, false)
            }
        }

        viewModel.musicList?.observe(this) { list ->
            bottomPlayerAdapter.setData(list)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUserKey()
        viewModel.addFavoritePlaylist()
    }
}