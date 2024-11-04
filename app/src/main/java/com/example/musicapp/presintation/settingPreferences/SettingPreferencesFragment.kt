package com.example.musicapp.presintation.settingPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.data.constant.GroupConst
import com.example.musicapp.databinding.FragmentSettingPreferencesBinding
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presintation.adapter.SettingsPerformancesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingPreferencesFragment: Fragment() {
    private lateinit var binding: FragmentSettingPreferencesBinding
    private lateinit var recyclerAdapter: SettingsPerformancesAdapter
    private val viewModel by viewModel<SettingsPreferencesViewModel>()

    private var getGroupAllFlag: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingPreferencesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        binding.progressIndicator.visibility = View.VISIBLE
        viewModel.getGroup()

        viewModel.getGroupAllResult.observe(viewLifecycleOwner) { liveData ->
            if (!getGroupAllFlag) {
                getGroupAllFlag = true

                liveData.observe(viewLifecycleOwner) { array ->
                    recyclerAdapter = SettingsPerformancesAdapter(array)
                    binding.recyclerView.adapter = recyclerAdapter

                    binding.progressIndicator.visibility = View.GONE
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.skip -> navController.navigate(R.id.action_settingPreferencesFragment_to_homeFragment)
            }

            true
        }
    }
}