package com.example.musicapp.presentation.author

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumsByAuthorId
import com.example.musicapp.domain.usecase.getGroup.GetGroupById
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAuthorId
import kotlinx.coroutines.launch

class AuthorViewModel(
    private val getGroupById: GetGroupById,
    private val getMusicsByAuthorId: GetMusicsByAuthorId,
    private val getAlbumsByAuthorId: GetAlbumsByAuthorId
): ViewModel() {
    private val getAuthorLiveData = MutableLiveData<Group>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val getAlbumLiveData = MutableLiveData<List<Album>>()

    val getAuthorResult: LiveData<Group> = getAuthorLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val getAlbumResult: LiveData<List<Album>> = getAlbumLiveData

    fun getAuthor(authorId: String) {
        viewModelScope.launch {
            getAuthorLiveData.value = getGroupById.execute(authorId)
        }
    }

    fun getMusic(authorId: String) {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicsByAuthorId.execute(authorId)
        }
    }

    fun getAlbum(authorId: String) {
        viewModelScope.launch {
            getAlbumLiveData.value = getAlbumsByAuthorId.execute(authorId)
        }
    }
}