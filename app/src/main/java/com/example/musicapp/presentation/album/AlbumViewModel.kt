package com.example.musicapp.presentation.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumById
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAlbumId
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val getAlbumById: GetAlbumById,
    private val getMusicsByAlbumId: GetMusicsByAlbumId
): ViewModel() {
    private val getAlbumLiveData = MutableLiveData<Album?>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()

    val getAlbumResult: LiveData<Album?> = getAlbumLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData

    fun getAlbum(id: String) {
        viewModelScope.launch {
            getAlbumLiveData.value = getAlbumById.execute(id)
        }
    }

    fun getMusic(albumId: String) {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicsByAlbumId.execute(albumId)
        }
    }
}
