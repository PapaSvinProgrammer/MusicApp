package com.example.musicapp.presentation.hate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.musicapp.databinding.FragmentHateBinding

class HateFragment: Fragment() {
    private lateinit var binding: FragmentHateBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}