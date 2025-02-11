package com.example.musicapp.presentation.authorMusicList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.FilterState
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAuthorId
import kotlinx.coroutines.launch

class MusicListViewModel(
    private val getMusicsByAuthorId: GetMusicsByAuthorId
): ViewModel() {
    private val musicsLiveData = MutableLiveData<List<Music>>()
    val musicsResult: LiveData<List<Music>> = musicsLiveData

    var filterMode = FilterState.BY_RATING

    fun getMusicsByRating(authorId: String) {
        filterMode = FilterState.BY_RATING

        viewModelScope.launch {
            musicsLiveData.value = getMusicsByAuthorId.executeOrderRating(authorId)
        }
    }

    fun getMusicsByAlbum(authorId: String) {
        filterMode = FilterState.BY_ALBUM

        viewModelScope.launch {
            musicsLiveData.value = getMusicsByAuthorId.executeOrderAlbum(authorId)
        }
    }

    fun getMusicsByName(authorId: String) {
        filterMode = FilterState.BY_NAME

        viewModelScope.launch {
            musicsLiveData.value = getMusicsByAuthorId.executeOrderName(authorId)
        }
    }
}