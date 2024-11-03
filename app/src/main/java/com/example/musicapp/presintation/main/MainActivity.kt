package com.example.musicapp.presintation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getDarkMode()
        viewModel.getEmail()
        viewModel.getUserKey()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _->
            if (destination.id == R.id.homeFragment ||
                destination.id == R.id.favoriteFragment ||
                destination.id == R.id.settingsFragment)
            {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
            else{
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}