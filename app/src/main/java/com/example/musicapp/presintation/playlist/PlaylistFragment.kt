package com.example.musicapp.presintation.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlaylistBinding
import com.example.musicapp.presintation.bottomSheet.FilterBottomSheet

class PlaylistFragment: Fragment() {
    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.newPlaylistLayout.setOnClickListener {
            //TODO
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    //TODO
                }
                R.id.filter -> createFilterBottomSheet()
            }

            true
        }
    }

    private fun createFilterBottomSheet() {
        val bottomSheetDialog = FilterBottomSheet()

        //TODO

        requireActivity().supportFragmentManager.let {
            bottomSheetDialog.show(it, FilterBottomSheet.TAG)
        }
    }
}