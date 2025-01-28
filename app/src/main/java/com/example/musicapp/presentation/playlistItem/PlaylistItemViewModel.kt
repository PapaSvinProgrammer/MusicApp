package com.example.musicapp.presentation.playlistItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.delete.DeletePlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetMusicFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistImage
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

private const val DEFAULT_PLAYLIST_ID = -1L

class PlaylistItemViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val updatePlaylistName: UpdatePlaylistName,
    private val updatePlaylistImage: UpdatePlaylistImage,
    private val deletePlaylistFromSQLite: DeletePlaylistFromSQLite,
    private val getMusicFromSQLite: GetMusicFromSQLite
): ViewModel() {
    private val getPlaylistLiveData = MutableLiveData<PlaylistResult?>()
    private val convertTextCountLiveData = MutableLiveData<String>()
    private val deletePlaylistLiveData = MutableLiveData<Boolean>()
    private val getMusicFlowState = MutableStateFlow(DEFAULT_PLAYLIST_ID)

    val getPlaylistResult: LiveData<PlaylistResult?> = getPlaylistLiveData
    val convertTextCountResult = convertTextCountLiveData
    val deletePlaylistResult: LiveData<Boolean> = deletePlaylistLiveData

    @OptIn(ExperimentalCoroutinesApi::class)
    var getMusicResult = getMusicFlowState.flatMapLatest {
        getMusicFromSQLite.getAllMusicFromPlaylist(it)
    }.asLiveData()

    fun getMusic(playlistId: Long) {
        getMusicFlowState.value = playlistId
    }

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