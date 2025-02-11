package com.example.musicapp.presentation.author

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumsByAuthorId
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAuthorId
import com.example.musicapp.domain.usecase.getGroup.GetGroup
import kotlinx.coroutines.launch

private const val DEFAULT_COUNT_MUSIC = 12L

class AuthorViewModel(
    private val getMusicsByAuthorId: GetMusicsByAuthorId,
    private val getAlbumsByAuthorId: GetAlbumsByAuthorId,
    private val getGroup: GetGroup
): ViewModel() {
    private val getAuthorLiveData = MutableLiveData<Group?>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val getAlbumLiveData = MutableLiveData<List<Album>>()

    val getAuthorResult: LiveData<Group?> = getAuthorLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val getAlbumResult: LiveData<List<Album>> = getAlbumLiveData

    fun getAuthor(authorId: String) {
        viewModelScope.launch {
            getAuthorLiveData.value = getGroup.getGroupById(authorId)
        }
    }

    fun getMusic(authorId: String) {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicsByAuthorId.executeOrderRating(
                authorId = authorId,
                limit = DEFAULT_COUNT_MUSIC
            )
        }
    }

    fun getAlbum(authorId: String) {
        viewModelScope.launch {
            getAlbumLiveData.value = getAlbumsByAuthorId.executeOrderRating(authorId)
        }
    }
}