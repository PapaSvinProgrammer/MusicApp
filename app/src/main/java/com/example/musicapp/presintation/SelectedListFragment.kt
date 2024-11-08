package com.example.musicapp.presintation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.databinding.FragmentSelectedListBinding
import com.example.musicapp.domain.module.Group
import com.example.musicapp.presintation.adapter.SelectedListAdapter

class SelectedListFragment: Fragment() {
    private lateinit var binding: FragmentSelectedListBinding
    private lateinit var recyclerAdapter: SelectedListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        val array = arguments?.getParcelableArrayList("ArrayGroup", Group::class.java)
        recyclerAdapter = SelectedListAdapter()
        recyclerAdapter.setData(array!!)

        binding.recyclerView.adapter = recyclerAdapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}