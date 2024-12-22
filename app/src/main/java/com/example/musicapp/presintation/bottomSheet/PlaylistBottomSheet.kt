package com.example.musicapp.presintation.bottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.BottomSheetPlaylistBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.state.SettingsPlaylist
import com.example.musicapp.domain.usecase.convert.ConvertAnyText
import com.example.musicapp.domain.usecase.convert.ConvertTextCountImpl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaylistBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "PlaylistBottomSheet"
        const val PLAYLIST_KEY = "PlaylistKey"
    }
    private lateinit var binding: BottomSheetPlaylistBinding
    private val convertTextCount = ConvertTextCountImpl(ConvertAnyText())

    private val settingsActionLiveData = MutableLiveData<SettingsPlaylist>()
    val settingsActionResult: LiveData<SettingsPlaylist> = settingsActionLiveData

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

        binding.downloadLayout.setOnClickListener {
            settingsActionLiveData.value = SettingsPlaylist.DOWNLOAD
        }

        binding.addMusicLayout.setOnClickListener {
            settingsActionLiveData.value = SettingsPlaylist.ADD_MUSIC
        }

        binding.editNameLayout.setOnClickListener {
            settingsActionLiveData.value = SettingsPlaylist.EDIT_NAME
        }

        binding.editImageLayout.setOnClickListener {
            settingsActionLiveData.value = SettingsPlaylist.EDIT_IMAGE
        }

        binding.deleteLayout.setOnClickListener {
            settingsActionLiveData.value = SettingsPlaylist.DELETE
        }
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
            binding.countTextView.text = convertTextCount.convertMusic(it.countMusic)
        }
    }
}