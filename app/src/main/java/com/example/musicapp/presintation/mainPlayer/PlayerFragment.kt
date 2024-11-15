package com.example.musicapp.presintation.mainPlayer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter

class PlayerFragment: Fragment() {
    private lateinit var binding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        val position = arguments?.getInt(BottomPlayerAdapter.POSITION_ARG)
        val array = arguments?.getParcelableArrayList(BottomPlayerAdapter.ARRAY_ARG, Music::class.java)

        Glide.with(binding.root)
            .load("https:" + array!![position!!].image)
            .into(binding.iconView)

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}