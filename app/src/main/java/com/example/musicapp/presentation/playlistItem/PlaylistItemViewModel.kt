package com.example.musicapp.presentation.playlistItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.usecase.room.delete.DeletePlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.domain.usecase.room.get.GetMusicsFromPlaylistSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistImage
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistItemViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val updatePlaylistName: UpdatePlaylistName,
    private val updatePlaylistImage: UpdatePlaylistImage,
    private val deletePlaylistFromSQLite: DeletePlaylistFromSQLite,
    private val getMusicsFromPlaylistSQLite: GetMusicsFromPlaylistSQLite,
    private val getCountMusic: GetCountMusic
): ViewModel() {
    private val _getPlaylist = MutableLiveData<PlaylistEntity>()
    private val _convertTextCount = MutableLiveData<String>()
    private val _deletePlaylist = MutableLiveData<Boolean>()
    private val _musics = MutableLiveData<List<MusicResult>>()
    private val _countMusicInPlaylist = MutableLiveData<Int>()

    val getPlaylist: LiveData<PlaylistEntity> = _getPlaylist
    val convertTextCount: LiveData<String> = _convertTextCount
    val deletePlaylist: LiveData<Boolean> = _deletePlaylist
    val musics: LiveData<List<MusicResult>> = _musics
    val countMusicInPlaylist: LiveData<Int> = _countMusicInPlaylist

    var isEmptyImage = false

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _getPlaylist.value = getPlaylistFromSQLite.getPlaylistById(playlistId)
        }
    }

    fun getMusicsFromPlaylist(playlistId: Long) {
        viewModelScope.launch {
            getMusicsFromPlaylistSQLite.getMusicsFromPlaylist(playlistId).collect {
                _musics.value = it
            }
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlaylistFromSQLite.execute(
                id = _getPlaylist.value?.id ?: -1L
            )
        }

        _deletePlaylist.value = true
    }

    fun saveName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistName.execute(
                name = name,
                id = _getPlaylist.value?.id ?: -1L
            )
        }
    }

    fun saveImage(uri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistImage.saveImage(
                url = uri,
                id = _getPlaylist.value?.id ?: -1L
            )
        }
    }

    fun deleteImage() {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistImage.deleteImage(
                id = getPlaylist.value?.id ?: -1L
            )
        }
    }

    fun getCountMusicInPlaylist(playlistId: Long) {
        viewModelScope.launch {
            getCountMusic.getCountMusicInPlaylist(playlistId).collect {
                _countMusicInPlaylist.value = it
            }
        }
    }
}