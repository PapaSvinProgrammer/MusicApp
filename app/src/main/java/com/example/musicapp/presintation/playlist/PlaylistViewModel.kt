package com.example.musicapp.presintation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.presintation.bottomSheet.FilterBottomSheet
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite
): ViewModel() {
    var currentFilterState: Int = FilterBottomSheet.BY_DEFAULT

    private val getPlaylistLiveData = MutableLiveData<List<PlaylistResult?>>()
    val getPlaylistResult: LiveData<List<PlaylistResult?>> = getPlaylistLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            getPlaylistLiveData.value = getPlaylistFromSQLite.executeToAll()
        }
    }
}