package com.example.musicapp.presentation.bottomSheetAddMusic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteMusicFromSQLite
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddMusicViewModel(
    private val searchMusic: SearchMusic,
    private val addMusicInSQLite: AddMusicInSQLite,
    private val deleteMusicFromSQLite: DeleteMusicFromSQLite
): ViewModel() {
    private val searchLiveData = MutableLiveData<List<Music>>()
    val searchResult: LiveData<List<Music>> = searchLiveData

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchMusic.execute(text)
        }
    }

    fun addMusicInPlaylist(music: Music, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            addMusicInSQLite.execute(
                music = music,
                playlistId = playlistId
            )
        }
    }

    fun deleteMusicFromPlaylist(musicId: String, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMusicFromSQLite.execute(musicId, playlistId)
        }
    }
}