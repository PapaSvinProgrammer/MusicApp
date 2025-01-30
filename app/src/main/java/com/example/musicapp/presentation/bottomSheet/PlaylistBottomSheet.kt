package com.example.musicapp.presentation.bottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.musicapp.R
import com.example.musicapp.databinding.BottomSheetPlaylistBinding
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.state.SettingsPlaylist
import com.example.musicapp.app.support.convertTextCount.ConvertAnyText
import com.example.musicapp.app.support.convertTextCount.ConvertTextCountImpl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaylistBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "PlaylistBottomSheet"
        const val PLAYLIST_KEY = "PlaylistKey"
    }

    private lateinit var binding: BottomSheetPlaylistBinding
    private val convertTextCount = ConvertTextCountImpl(ConvertAnyText())
    private var listener: ((SettingsPlaylist) -> Unit)? = null

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
            listener?.invoke(SettingsPlaylist.DOWNLOAD)
            dismiss()
        }

        binding.addMusicLayout.setOnClickListener {
            listener?.invoke(SettingsPlaylist.ADD_MUSIC)
            dismiss()
        }

        binding.editNameLayout.setOnClickListener {
            listener?.invoke(SettingsPlaylist.EDIT_NAME)
            dismiss()
        }

        binding.editImageLayout.setOnClickListener {
            listener?.invoke(SettingsPlaylist.EDIT_IMAGE)
            dismiss()
        }

        binding.deleteLayout.setOnClickListener {
            listener?.invoke(SettingsPlaylist.DELETE)
            dismiss()
        }
    }

    @SuppressLint("NewApi", "SetTextI18n")
    override fun onStart() {
        super.onStart()

        val currentPlaylist = arguments?.getParcelable(PLAYLIST_KEY, Album::class.java)

        currentPlaylist?.let {
            val countMusicStr = it.countMusic.toString()

            Glide.with(binding.root)
                .load(it.image)
                .error(R.drawable.ic_error_image)
                .into(binding.imageView)

            binding.playlistTextView.text = it.name
            binding.countTextView.text = countMusicStr + convertTextCount.convertMusic(it.countMusic)
        }
    }

    fun setOnClickListener(listener: ((SettingsPlaylist) -> Unit)) {
        this.listener = listener
    }
}