package com.example.musicapp.presintation.bottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.musicapp.databinding.BottomSheetPlayerSettingsBinding
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.SettingsPlayer
import com.example.musicapp.presintation.pagerAdapter.BottomPlayerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlayerBottomSheet: BottomSheetDialogFragment() {
    companion object {
        const val TAG = "PlayerBottomSheetDialog"
        const val CURRENT_MUSIC = "currentObject"
        const val IS_FAVORITE = "isFavorite"
        const val IS_DOWNLOADED = "isDownloaded"
    }

    private lateinit var binding: BottomSheetPlayerSettingsBinding

    private val settingsStateLiveData = MutableLiveData<SettingsPlayer>()
    val settingsStateResult: LiveData<SettingsPlayer> = settingsStateLiveData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetPlayerSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.favoriteLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.LIKE
        }

        binding.addInPlaylistLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.ADD_TO_PLAYLIST
        }

        binding.shareLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.SHARE
        }

        binding.playNextLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.PLAY_NEXT
        }

        binding.addToQueueLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.ADD_TO_QUEUE
        }

        binding.moveToGroupLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.MOVE_TO_GROUP
        }

        binding.moveToAlbumLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.MOVE_TO_ALBUM
        }

        binding.deleteLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.DELETE
        }

        binding.downloadLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.DOWNLOAD
        }

        binding.aboutMusicLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.INFO
        }

        binding.hateLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.HATE
        }

        binding.reportLayout.setOnClickListener {
            settingsStateLiveData.value = SettingsPlayer.REPORT_PROBLEM
        }
    }

    @SuppressLint("NewApi")
    override fun onStart() {
        super.onStart()

        val currentMusic = arguments?.getParcelable(CURRENT_MUSIC, Music::class.java)
        val isFavorite = arguments?.getBoolean(IS_FAVORITE)
        val isDownloaded = arguments?.getBoolean(IS_DOWNLOADED)
        val parent = arguments?.getString(BottomPlayerAdapter.PARENT_ARG)

        if (currentMusic != null) initTopView(currentMusic)

        if (isFavorite != null) binding.iconFavorite.isSelected = isFavorite

        if (isDownloaded != null) initDownloadedView(isDownloaded)

        if (parent == BottomPlayerAdapter.PARENT_ARG_HOME) {
            binding.addToQueueLayout.visibility = View.GONE
            binding.playNextLayout.visibility = View.GONE
        }
    }

    private fun initTopView(currentMusic: Music) {
        Glide.with(binding.root)
            .load(currentMusic.imageHigh)
            .into(binding.imageView)

        binding.groupTextView.text = currentMusic.group
        binding.musicTextView.text = currentMusic.name
    }

    private fun initDownloadedView(isDownloaded: Boolean) {
        when (isDownloaded) {
            true -> binding.downloadLayout.visibility = View.GONE
            false -> binding.deleteLayout.visibility = View.GONE
        }
    }
}