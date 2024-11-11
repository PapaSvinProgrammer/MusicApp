package com.example.musicapp.presintation.pagerAdapter

import androidx.core.bundle.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.presintation.bottomPlayer.BottomPlayerFragment

class BottomPlayerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 100

    override fun createFragment(position: Int): Fragment {
        val fragment = BottomPlayerFragment()
        fragment.arguments = Bundle().apply {
            putString(BottomPlayerFragment.NAME_ARG, "Name$position")
            putString(BottomPlayerFragment.GROUP_ARG, "Name$position")
        }

        return fragment
    }
}