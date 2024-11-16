package com.example.musicapp.presintation.mainPlayer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presintation.pagerAdapter.PlayerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerFragment: Fragment() {
    private val playerAdapter by lazy { PlayerAdapter() }
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

        if (array != null && position != null) {
            val currentMusic = array[position]

            lifecycleScope.launch(Dispatchers.Main) {
                binding.viewPager.adapter = playerAdapter
                playerAdapter.setData(array)
                binding.viewPager.currentItem = position
            }

            binding.groupTextView.text = currentMusic.group
            binding.musicTextView.text = currentMusic.name
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }
}