package com.example.musicapp.presintation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.room.add.AddPlaylistInSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.presintation.bottomSheet.FilterBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val addPlaylistInSQLite: AddPlaylistInSQLite
): ViewModel() {
    private val filterFlowState = MutableStateFlow<Int>(FilterBottomSheet.BY_DEFAULT)
    var currentFilterState: Int = FilterBottomSheet.BY_DEFAULT

    @OptIn(ExperimentalCoroutinesApi::class)
    val getPlaylistResult = filterFlowState.flatMapLatest {
        getPlaylistFromSQLite.executeToAll(it)
    }.asLiveData()

    fun updateFilter() {
        filterFlowState.value = currentFilterState
    }

    fun addPlaylist(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistInSQLite.execute(
                name = name,
                image = ""
            )
        }
    }
}