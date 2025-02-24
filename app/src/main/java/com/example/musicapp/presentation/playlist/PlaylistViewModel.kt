package com.example.musicapp.presentation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.state.FilterState
import com.example.musicapp.domain.usecase.room.add.AddPlaylistInSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchPlaylistLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val addPlaylistInSQLite: AddPlaylistInSQLite,
    private val searchPlaylistLocal: SearchPlaylistLocal
): ViewModel() {
    private val filterFlowState = MutableStateFlow(FilterState.BY_DEFAULT)
    private val searchLiveData = MutableLiveData<List<PlaylistEntity>>()

    val filterFlowStateResult: StateFlow<FilterState> = filterFlowState
    val searchResult: LiveData<List<PlaylistEntity>> = searchLiveData

    @OptIn(ExperimentalCoroutinesApi::class)
    val getPlaylistResult = filterFlowState.flatMapLatest {
        when (it) {
            FilterState.BY_NAME -> getPlaylistFromSQLite.getPlaylistsOrderName()
            FilterState.BY_DATE -> getPlaylistFromSQLite.getPlaylistsOrderDate()
            else -> getPlaylistFromSQLite.getPlaylistsOrderId()
        }
    }.asLiveData()

    fun updateFilter(filterState: FilterState) {
        filterFlowState.value = filterState
    }

    fun addPlaylist(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistInSQLite.execute(
                name = name,
                image = ""
            )
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchPlaylistLocal.execute(text)
        }
    }
}