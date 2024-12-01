package com.example.musicapp.presintation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSettingsBinding
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.presintation.dialog.ExitDialog
import com.example.musicapp.presintation.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().apply {
            bindService(
                Intent(this, PlayerService::class.java),
                viewModel.connectionToPlayerService,
                Context.BIND_AUTO_CREATE
            )
        }

        val navController = view.findNavController()
        val requestViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        binding.userNameView.text = requestViewModel.emailResult

        if (requestViewModel.darkModeResult) {
            binding.switchDarkMode.isChecked = true
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.saveDarkMode(true)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.saveDarkMode(false)
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exit -> {
                    ExitDialog(navController, viewModel).show(parentFragmentManager, "TAG")
                    true
                }
                else -> {
                    true
                }
            }
        }

        binding.storageLayout.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_storageFragment)
        }
    }
}