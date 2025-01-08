package com.example.musicapp.presentation.home

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.state.SearchFilterState
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumAll
import com.example.musicapp.domain.usecase.getGroup.GetGroupAll
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import com.example.musicapp.domain.usecase.room.add.AddPlaylistInSQLite
import com.example.musicapp.domain.usecase.search.SearchAll
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMusicAll: GetMusicAll,
    private val getAlbumsAll: GetAlbumAll,
    private val getGroupAll: GetGroupAll,
    private val searchAll: SearchAll
): ViewModel() {
    lateinit var isPlayService: LiveData<Boolean>
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private var searchList = arrayListOf<Music>()

    private val statePlayerLiveData = MutableLiveData<StatePlayer>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val getGroupLiveData = MutableLiveData<List<Group>>()
    private val getAlbumLiveData = MutableLiveData<List<Album>>()
    private val searchLiveData = MutableLiveData<List<Music>?>()
    private val permissionForSearchLiveData = MutableLiveData<Int>()
    private val searchFilterStateLiveData = MutableLiveData<SearchFilterState>()

    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val searchResult: LiveData<List<Music>?> = searchLiveData
    val getGroupResult: LiveData<List<Group>> = getGroupLiveData
    val getAlbumResult: LiveData<List<Album>> = getAlbumLiveData
    val permissionForSearchResult: LiveData<Int> = permissionForSearchLiveData
    val searchFilterStateResult: LiveData<SearchFilterState> = searchFilterStateLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun dataForSearch() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicAll.execute()
        }

        viewModelScope.launch {
            getGroupLiveData.value = getGroupAll.execute()
        }

        viewModelScope.launch {
            getAlbumLiveData.value = getAlbumsAll.execute()
        }
    }

    fun search(text: String) {
        if (permissionForSearchResult.value != 3) return

        viewModelScope.launch {
            searchLiveData.value = searchAll.execute(
                text = text,
                list = searchList
            )
        }
    }

    fun setPermissionIndex(index: Int) {
        permissionForSearchLiveData.value = index
    }

    fun addMusicInSearchList(list: List<Music>) {
        searchList.addAll(list)
    }

    fun addAlbumInSearchList(list: List<Album>) {
        searchList.addAll(convertAlbumList(list))
    }

    fun addGroupInSearchList(list: List<Group>) {
        searchList.addAll(convertGroupList(list))
    }

    fun setAllInSearchList() {
        searchList.clear()
        searchList.addAll(getMusicLiveData.value ?: listOf())
        searchList.addAll(convertAlbumList(getAlbumLiveData.value ?: listOf()))
        searchList.addAll(convertGroupList(getGroupLiveData.value ?: listOf()))
    }

    fun setMusicInSearchList() {
        searchList.clear()
        searchList.addAll(getMusicLiveData.value ?: listOf())
    }

    fun setAlbumInSearchList() {
        searchList.clear()
        searchList.addAll(convertAlbumList(getAlbumLiveData.value ?: listOf()))
    }

    fun setGroupInSearchList() {
        searchList.clear()
        searchList.addAll(convertGroupList(getGroupLiveData.value ?: listOf()))
    }

    fun setSearchFilterState(state: SearchFilterState) {
        searchFilterStateLiveData.value = state
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            isPlayService = binder.isPlay()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }

    private fun convertGroupList(list: List<Group>): List<Music> {
        return list.map {
            Music(
                group = it.name,
                imageGroup = it.image,
                groupId = it.id
            )
        }.toList()
    }

    private fun convertAlbumList(list: List<Album>): List<Music> {
        return list.map {
            Music(
                albumName = it.name,
                albumId = it.id,
                imageHigh = it.image
            )
        }.toList()
    }
}