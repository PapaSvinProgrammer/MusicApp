package com.example.musicapp.presintation.bottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.BottomSheetPlaylistBinding
import com.example.musicapp.domain.module.Album
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaylistBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "PlaylistBottomSheet"
        const val PLAYLIST_KEY = "PlaylistKey"
    }
    private lateinit var binding: BottomSheetPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()

        val currentPlaylist = arguments?.getParcelable(PLAYLIST_KEY, Album::class.java)

        currentPlaylist?.let {
            Glide.with(binding.root)
                .load(it.image)
                .into(binding.imageView)

            binding.playlistTextView.text = it.name

        }
    }
}