package com.example.musicapp.presentation.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import kotlinx.coroutines.launch

private const val DOWNLOAD_LIMIT = 12

class DownloadViewModel(
    private val getDownloadedMusic: GetDownloadedMusic
): ViewModel() {
    private val musicLiveData = MutableLiveData<List<Music>>()
    val musicResult: LiveData<List<Music>> = musicLiveData

    fun getDownloadMusic() {
        viewModelScope.launch {
            musicLiveData.value = getDownloadedMusic.getDownloadsLimit(DOWNLOAD_LIMIT)
        }
    }
}