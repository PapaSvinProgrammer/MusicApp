package com.example.musicapp.presintation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.domain.player.StatePlayer
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bottomPlayerAdapter: BottomPlayerAdapter
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        setupViewPager(binding.bottomViewPager)
        bottomPlayerAdapter = BottomPlayerAdapter(navController, viewModel)

        binding.progressIndicator.visibility = View.VISIBLE
        if (viewModel.getMusicResult.value == null) {
            viewModel.getMusic()
        }

        binding.mainPlayButton.setOnClickListener {
            when (binding.mainPlayButton.isSelected) {
                true -> viewModel.setStatePlayer(StatePlayer.PAUSE)
                false -> viewModel.setStatePlayer(StatePlayer.PLAY)
            }
        }

        viewModel.statePlayer.observe(viewLifecycleOwner) {
            when (it) {
                StatePlayer.PLAY -> {
                    binding.lottieAnim.playAnimation()
                    binding.mainPlayButton.isSelected = true
                }

                StatePlayer.PAUSE -> {
                    binding.lottieAnim.pauseAnimation()
                    binding.mainPlayButton.isSelected = false
                }

                StatePlayer.PREVIOUS -> TODO()

                StatePlayer.NEXT -> TODO()
            }
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { liveData->
            liveData.observe(viewLifecycleOwner) { array->
                viewModel.lastDownloadArray = array

                bottomPlayerAdapter.setData(array)
                binding.bottomViewPager.adapter = bottomPlayerAdapter

                binding.viewPagerLayout.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setupViewPager(viewPager: ViewPager2) {
        viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
        }

        val offsetPx = resources
            .getDimensionPixelOffset(R.dimen.viewpager_item_visible)
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