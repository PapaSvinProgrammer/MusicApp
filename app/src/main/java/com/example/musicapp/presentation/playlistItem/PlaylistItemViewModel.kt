package com.example.musicapp.presentation.playlistItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.delete.DeletePlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistImage
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistItemViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val updatePlaylistName: UpdatePlaylistName,
    private val updatePlaylistImage: UpdatePlaylistImage,
    private val deletePlaylistFromSQLite: DeletePlaylistFromSQLite
): ViewModel() {
    private val getPlaylistLiveData = MutableLiveData<PlaylistResult?>()
    private val convertTextCountLiveData = MutableLiveData<String>()
    private val deletePlaylistLiveData = MutableLiveData<Boolean>()

    val getPlaylistResult: LiveData<PlaylistResult?> = getPlaylistLiveData
    val convertTextCountResult = convertTextCountLiveData
    val deletePlaylistResult: LiveData<Boolean> = deletePlaylistLiveData

    fun getPlaylist(id: Long) {
        viewModelScope.launch {
            getPlaylistLiveData.value = getPlaylistFromSQLite.getById(id)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlaylistFromSQLite.execute(
                id = getPlaylistLiveData.value?.playlistEntity?.id ?: -1L
            )
        }

        deletePlaylistLiveData.value = true
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