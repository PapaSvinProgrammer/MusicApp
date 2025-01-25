package com.example.musicapp.presentation.playlistFavoriteAuthorList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.state.FilterState
import com.example.musicapp.domain.usecase.room.get.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchGroupLocal
import kotlinx.coroutines.launch

class FavoriteAuthorListViewModel(
    private val getAuthorsFromSQLite: GetAuthorsFromSQLite,
    private val searchGroupLocal: SearchGroupLocal
): ViewModel() {
    private val playlistsLiveData = MutableLiveData<List<AuthorEntity>>()
    private val searchLiveData = MutableLiveData<List<AuthorEntity>>()

    val playlistResult: LiveData<List<AuthorEntity>> = playlistsLiveData
    val searchResult: LiveData<List<AuthorEntity>> = searchLiveData

    fun getAuthors() {

        viewModelScope.launch {
            playlistsLiveData.value = getAuthorsFromSQLite.execute()
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchGroupLocal.execute(text)
        }
    }
}