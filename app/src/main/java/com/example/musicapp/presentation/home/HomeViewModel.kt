package com.example.musicapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.data.constant.PlaylistConst
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.SearchFilterState
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetRandomMusic
import com.example.musicapp.domain.usecase.room.add.AddPlaylistInSQLite
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchAlbum
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchAll
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchGroup
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DEFAULT_COUNT_MUSIC = 3L

class HomeViewModel(
    private val searchMusic: SearchMusic,
    private val searchAlbum: SearchAlbum,
    private val searchGroup: SearchGroup,
    private val searchAll: SearchAll,
    private val addPlaylistInSQLite: AddPlaylistInSQLite,
    private val getRandomMusic: GetRandomMusic
): ViewModel() {
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()
    private val searchLiveData = MutableLiveData<List<Music>>()
    private val randomMusicsLIveData = MutableLiveData<List<Music>>()

    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val searchResult: LiveData<List<Music>> = searchLiveData
    val randomMusicsResult: LiveData<List<Music>> = randomMusicsLIveData

    private var searchFilterState = SearchFilterState.ALL

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun search(text: String) {
        when (searchFilterState) {
            SearchFilterState.ALL -> searchAll(text)
            SearchFilterState.MUSIC -> searchMusicName(text)
            SearchFilterState.ALBUM -> searchAlbumName(text)
            SearchFilterState.AUTHOR -> searchAuthorName(text)
        }
    }

    fun setSearchFilterState(state: SearchFilterState) {
        searchFilterState = state
    }

    fun addFavoritePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistInSQLite.execute(
                name = PlaylistConst.NAME_PLAYLIST_FAVORITE,
                image = PlaylistConst.URL_PLAYLIST_FAVORITE
            )
        }
    }

    fun getRandomMusic() {
        viewModelScope.launch {
            randomMusicsLIveData.value = getRandomMusic.getMusics(DEFAULT_COUNT_MUSIC)
        }
    }

    fun setMediaItems(list: List<Music>) {
        viewModelScope.launch {
            MediaControllerManager.setMediaItems(list)
        }
    }

    private fun searchAuthorName(text: String) {
        viewModelScope.launch {
            searchLiveData.value = convertAuthorData(searchGroup.execute(text))
        }
    }

    private fun searchAlbumName(text: String) {
        viewModelScope.launch {
            searchLiveData.value = convertAlbumData(searchAlbum.execute(text))
        }
    }

    private fun searchMusicName(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchMusic.execute(text)
        }
    }

    private fun searchAll(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchAll.execute(text)
        }
    }

    private fun convertAuthorData(list: List<Group>): List<Music> {
        return list.map {
            Music(
                group = it.name,
                imageGroup = it.image,
                groupId = it.id
            )
        }.toList()
    }

    private fun convertAlbumData(list: List<Album>): List<Music> {
        return list.map {
            Music(
                albumName = it.name,
                albumId = it.id,
                imageHigh = it.image,
                group = it.groupName
            )
        }.toList()
    }
}