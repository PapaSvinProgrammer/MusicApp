package com.example.musicapp.presentation.downloadList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchDownloadedLocal
import kotlinx.coroutines.launch

class DownloadListViewModel(
    private val getDownloadedMusic: GetDownloadedMusic,
    private val searchDownloadedLocal: SearchDownloadedLocal
): ViewModel() {
    private val musicLiveData = MutableLiveData<List<Music>>()
    private val searchLiveData = MutableLiveData<List<Music>>()

    val musicResult: LiveData<List<Music>> = musicLiveData
    val searchResult: LiveData<List<Music>> = searchLiveData

    fun getDownloadedMusic() {
        viewModelScope.launch {
            musicLiveData.value = getDownloadedMusic.getDownloads()
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchDownloadedLocal.execute(text)
        }
    }
}