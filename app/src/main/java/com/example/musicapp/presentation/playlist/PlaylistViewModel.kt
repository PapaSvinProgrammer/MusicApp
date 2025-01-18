package com.example.musicapp.presentation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.usecase.room.add.AddPlaylistInSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchPlaylistLocal
import com.example.musicapp.presentation.bottomSheet.FilterBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val addPlaylistInSQLite: AddPlaylistInSQLite,
    private val searchPlaylistLocal: SearchPlaylistLocal
): ViewModel() {
    private val filterFlowState = MutableStateFlow(FilterBottomSheet.BY_DEFAULT)
    private val searchLiveData = MutableLiveData<List<PlaylistResult?>>()

    var currentFilterState: Int = FilterBottomSheet.BY_DEFAULT
    val searchResult: LiveData<List<PlaylistResult?>> = searchLiveData

    @OptIn(ExperimentalCoroutinesApi::class)
    val getPlaylistResult = filterFlowState.flatMapLatest {
        when (it) {
            FilterBottomSheet.BY_ALPHABET -> getPlaylistFromSQLite.getAllByName()
            FilterBottomSheet.BY_DATE_CREATE -> getPlaylistFromSQLite.getAllByDate()
            else -> getPlaylistFromSQLite.getAllById()
        }
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

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchPlaylistLocal.execute(
                list = getPlaylistResult.value,
                text = text
            )
        }
    }
}