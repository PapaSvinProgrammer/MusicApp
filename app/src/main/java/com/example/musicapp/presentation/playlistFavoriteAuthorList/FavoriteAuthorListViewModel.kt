package com.example.musicapp.presentation.playlistFavoriteAuthorList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.usecase.room.get.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchGroupLocal
import kotlinx.coroutines.launch

class FavoriteAuthorListViewModel(
    private val getAuthorsFromSQLite: GetAuthorsFromSQLite,
    private val searchGroupLocal: SearchGroupLocal
): ViewModel() {
    private val _playlists = MutableLiveData<List<AuthorEntity>>()
    private val _search = MutableLiveData<List<AuthorEntity>>()

    val playlistResult: LiveData<List<AuthorEntity>> = _playlists
    val searchResult: LiveData<List<AuthorEntity>> = _search

    fun getAuthors() {
        viewModelScope.launch {
            getAuthorsFromSQLite.execute().collect {
                _playlists.value = it
            }
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            _search.value = searchGroupLocal.execute(text)
        }
    }
}