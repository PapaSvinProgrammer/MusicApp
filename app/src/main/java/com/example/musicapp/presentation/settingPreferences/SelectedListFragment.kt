package com.example.musicapp.presentation.settingPreferences

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.databinding.FragmentSelectedListBinding
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presentation.recyclerAdapter.SelectedListAdapter

class SelectedListFragment: Fragment() {
    private lateinit var binding: FragmentSelectedListBinding
    private val selectedListAdapter by lazy {
        SelectedListAdapter(
            context = requireActivity().applicationContext
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = view.findNavController()
        binding.recyclerView.adapter = selectedListAdapter

        val array = arguments?.getParcelableArrayList(SettingPreferencesFragment.ARRAY_ARG, Group::class.java)
        selectedListAdapter.setData(array!!)


        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}