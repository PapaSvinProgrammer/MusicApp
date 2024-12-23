package com.example.musicapp.presentation.playlistFavorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import kotlinx.coroutines.launch

class PlaylistFavoriteViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite
): ViewModel() {
    private val getPlaylistLiveData = MutableLiveData<PlaylistResult?>()
    val getPlaylistResult: LiveData<PlaylistResult?> = getPlaylistLiveData

    fun getPlaylist() {
        viewModelScope.launch {
            getPlaylistLiveData.value = getPlaylistFromSQLite.getById(1)
        }
    }
}