package com.example.musicapp.presentation.bottomSheetPlaylistFavorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.databinding.BottomSheetPlaylistFavoriteSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaylistFavoriteBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "PlaylistFavoriteBottomSheet"
        const val MUSIC_KEY = "currentObject"
    }

    private lateinit var binding: BottomSheetPlaylistFavoriteSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPlaylistFavoriteSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}