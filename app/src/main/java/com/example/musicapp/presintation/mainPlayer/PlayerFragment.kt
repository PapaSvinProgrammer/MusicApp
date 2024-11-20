package com.example.musicapp.presintation.mainPlayer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.StatePlayer
import com.example.musicapp.presintation.home.HomeViewModel
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.example.musicapp.presintation.pagerAdapter.PlayerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlayerFragment: Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var arrayViewPager: ArrayList<Music>

    private val playerAdapter by lazy { PlayerAdapter() }
    private val homeViewModel: HomeViewModel by activityViewModel()

    private var currentPosition: Int = 0

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

        lifecycleScope.launch {
            setupViewPager(binding.viewPager)
        }

        val position = arguments?.getInt(BottomPlayerAdapter.POSITION_ARG)
        val array = arguments?.getParcelableArrayList(BottomPlayerAdapter.ARRAY_ARG, Music::class.java)

        if (array != null && position != null) {
            currentPosition = position
            arrayViewPager = array

            val currentMusic = array[position]

            lifecycleScope.launch(Dispatchers.Main) {
                binding.viewPager.adapter = playerAdapter
                playerAdapter.setData(array)
                binding.viewPager.currentItem = position
            }

            binding.groupTextView.text = currentMusic.group
            binding.musicTextView.text = currentMusic.name
        }

        homeViewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> playMusic()
                StatePlayer.PAUSE -> pauseMusic()
                StatePlayer.PREVIOUS -> previousMusic()
                StatePlayer.NEXT -> nextMusic()
            }
        }

        binding.nextView.setOnClickListener {
            homeViewModel.setStatePlayer(StatePlayer.NEXT)
        }

        binding.previousView.setOnClickListener {
            homeViewModel.setStatePlayer(StatePlayer.PREVIOUS)
        }

        binding.playStopView.setOnClickListener {
            if (binding.playStopView.isSelected) {
                homeViewModel.setStatePlayer(StatePlayer.PAUSE)
            }
            else {
                homeViewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    private fun nextMusic() {
        binding.viewPager.currentItem += 1
        val newObj = arrayViewPager[binding.viewPager.currentItem]

        binding.musicTextView.text = newObj.name
        binding.groupTextView.text = newObj.group
    }

    private fun previousMusic() {
        binding.viewPager.currentItem -= 1
        val newObj = arrayViewPager[binding.viewPager.currentItem]

        binding.musicTextView.text = newObj.name
        binding.groupTextView.text = newObj.group
    }

    private fun pauseMusic() {
        binding.playStopView.isSelected = false
    }

    private fun playMusic() {
        binding.playStopView.isSelected = true
    }

    @SuppressLint("ResourceType")
    private fun setupViewPager(viewPager: ViewPager2) {
        viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
        }

        val offsetPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_item_visible_main)
            .dpToPx(resources.displayMetrics)

        viewPager.setPadding(offsetPx, 0, offsetPx, 0)

        val pageMarginPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_current_item_horizontal_margin)
            .dpToPx(resources.displayMetrics)

        viewPager.setPageTransformer(
            MarginPageTransformer(pageMarginPx)
        )
    }

    private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()
}