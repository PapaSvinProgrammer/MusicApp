package com.example.musicapp.presintation.playlistItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistImage
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistItemViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val updatePlaylistName: UpdatePlaylistName,
    private val updatePlaylistImage: UpdatePlaylistImage
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

    fun deletePlaylist() {

    }

    fun saveName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistName.execute(
                name = name,
                id = getPlaylistLiveData.value?.playlistEntity?.id ?: -1L
            )
        }
    }

    fun saveImage(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistImage.saveImage(
                url = uri,
                id = getPlaylistLiveData.value?.playlistEntity?.id ?: -1L
            )
        }
    }

    fun deleteImage() {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistImage.deleteImage(
                id = getPlaylistResult.value?.playlistEntity?.id ?: -1L
            )
        }
    }
}