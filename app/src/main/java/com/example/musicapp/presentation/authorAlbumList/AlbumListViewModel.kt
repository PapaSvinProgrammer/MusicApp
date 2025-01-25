package com.example.musicapp.presentation.authorAlbumList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.state.FilterState
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumsByAuthorId
import kotlinx.coroutines.launch

class AlbumListViewModel(
    private val getAlbumsByAuthorId: GetAlbumsByAuthorId
): ViewModel() {
    private val albumLiveData = MutableLiveData<List<Album>>()
    val albumResult: LiveData<List<Album>> = albumLiveData

    var filterMode = FilterState.BY_DEFAULT

    fun getAlbumsByDate(authorId: String) {
        filterMode = FilterState.BY_DATE
        viewModelScope.launch {
            albumLiveData.value = getAlbumsByAuthorId.executeOrderData(authorId)
        }
    }

    fun getAlbumsByName(authorId: String) {
        filterMode = FilterState.BY_NAME
        viewModelScope.launch {
            albumLiveData.value = getAlbumsByAuthorId.executeOrderName(authorId)
        }
    }

    fun getAlbumsByRating(authorId: String) {
        filterMode = FilterState.BY_RATING
        viewModelScope.launch {
            albumLiveData.value = getAlbumsByAuthorId.executeOrderRating(authorId)
        }
    }
}