package com.example.musicapp.presintation.settingPreferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding

class SettingPreferencesFragment: Fragment() {
    private lateinit var binding: FragmentSettingPreferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingPreferencesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}