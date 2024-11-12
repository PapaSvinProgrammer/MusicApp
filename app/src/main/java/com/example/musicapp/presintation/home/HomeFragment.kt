package com.example.musicapp.presintation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.example.musicapp.databinding.FragmentHomeBinding
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
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

        binding.progressIndicator.visibility = View.VISIBLE
        if (viewModel.getMusicResult.value == null) {
            viewModel.getMusic()
        }

        viewModel.getMusicResult.observe(viewLifecycleOwner) { liveData->
            liveData.observe(viewLifecycleOwner) { array->
                binding.bottomViewPager.adapter = BottomPlayerAdapter(
                    list = array,
                    fragment = this
                )

                binding.viewPagerLayout.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE
            }
        }
    }
}