package com.example.musicapp.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSettingsBinding
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(0, systemBars.top, 0, 0)
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = view.findNavController()

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveDarkMode(true)
            }
            else {
                saveDarkMode(false)
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
            if (binding.switchDarkMode.isChecked != it) {
                binding.switchDarkMode.isChecked = it
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (viewModel.getDarkModeStateResult.value == null) {
            viewModel.getDarkMode()
        }

        if (viewModel.getEmailResult.value == null) {
            viewModel.getEmail()
        }
    }

    private fun saveDarkMode(state: Boolean) {
        viewModel.saveDarkMode(state)
    }
}