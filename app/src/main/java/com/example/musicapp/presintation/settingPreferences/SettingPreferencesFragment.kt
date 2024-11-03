package com.example.musicapp.presintation.settingPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.presintation.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingPreferencesFragment: Fragment() {
    private lateinit var binding: FragmentSettingPreferencesBinding
    private val viewModel by viewModel<SettingsPreferencesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingPreferencesBinding.inflate(layoutInflater, container, false)
        Log.d("RRRR", "Create")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("RRRR", "Created")
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.skip -> navController.navigate(R.id.action_settingPreferencesFragment_to_homeFragment)
            }

            true
        }
    }
}