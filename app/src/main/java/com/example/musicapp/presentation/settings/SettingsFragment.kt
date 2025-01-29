package com.example.musicapp.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSettingsBinding
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.presentation.dialog.ExitDialog
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

        binding.hateLayout.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_hateFragment)
        }

        binding.aboutLayout.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_aboutFragment2)
        }

        binding.changePreferencesLayout.setOnClickListener {
            navController.navigate(R.id.action_global_settingPreferencesFragment2)
        }

        viewModel.getEmailResult.observe(viewLifecycleOwner) {
            binding.userNameView.text = it
        }

        viewModel.getDarkModeStateResult.observe(viewLifecycleOwner) {
            binding.switchDarkMode.isChecked = it
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.getDarkMode()
        viewModel.getEmail()
    }
}