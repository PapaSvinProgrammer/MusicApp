package com.example.musicapp.presintation.pagerAdapter

import androidx.core.bundle.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.presintation.bottomPlayer.BottomPlayerFragment

class BottomPlayerAdapter(
    private val list: List<Music>,
    fragment: Fragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        val music = list[position]

        val fragment = BottomPlayerFragment()
        fragment.arguments = Bundle().apply {
            putString(BottomPlayerFragment.NAME_ARG, music.name)
            putString(BottomPlayerFragment.GROUP_ARG, music.group)
            putString(BottomPlayerFragment.IMAGE_ARG, music.image)
        }

        return fragment
    }
}