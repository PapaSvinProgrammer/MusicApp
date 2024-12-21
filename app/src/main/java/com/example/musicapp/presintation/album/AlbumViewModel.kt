package com.example.musicapp.presintation.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite
): ViewModel() {
    private val getPlaylistLiveData = MutableLiveData<PlaylistResult?>()
    private val convertTextCountLiveData = MutableLiveData<String>()

    val getPlaylistResult: LiveData<PlaylistResult?> = getPlaylistLiveData
    val convertTextCountResult = convertTextCountLiveData

    fun getPlaylist(id: Long) {
        viewModelScope.launch {
            getPlaylistLiveData.value = getPlaylistFromSQLite.getById(id)
        }
    }
}