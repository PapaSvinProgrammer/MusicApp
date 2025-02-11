package com.example.musicapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getMusic.GetRandomMusic
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteMusicFromSQLite
import com.example.musicapp.domain.usecase.room.find.FindMusicInSQLite
import kotlinx.coroutines.launch

private const val DEFAULT_COUNT_MUSIC = 3L

class MainViewModel(
    private val getDarkModeState: GetDarkModeState,
    private val addMusicInSQLite: AddMusicInSQLite,
    private val deleteMusicFromSQLite: DeleteMusicFromSQLite,
    private val getRandomMusic: GetRandomMusic,
    private val findMusicInSQLite: FindMusicInSQLite
): ViewModel() {
    var countMusicList = 0
    var networkConnection: Int? = null
    var isInitMediaController = false

    private val startDownloadLiveData = MutableLiveData<Boolean>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val isFavoriteLiveData = MutableLiveData<MusicResult?>()
    private val getDarkModeLiveData = MutableLiveData<Boolean>()

    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val startDownloadResult: LiveData<Boolean> = startDownloadLiveData
    val getDarkModeResult: LiveData<Boolean> = getDarkModeLiveData


    fun getDarkMode() {
        viewModelScope.launch {
            getDarkModeState.execute().collect {
                getDarkModeLiveData.value = it
            }
        }
    }

    fun setStartState(state: Boolean) {
        startDownloadLiveData.value = state
    }

    fun deleteMusicFromSQLite(musicId: String) {
        viewModelScope.launch {
            deleteMusicFromSQLite.execute(musicId)
        }
    }

    fun addMusicInSQLite(music: Music) {
        viewModelScope.launch {
            addMusicInSQLite.execute(music)
        }
    }

    fun getRandomMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getRandomMusic.getMusics(DEFAULT_COUNT_MUSIC)
        }
    }

    fun putRandomMusic() {
        viewModelScope.launch {
            val music = getRandomMusic.getMusicSingle()

           MediaControllerManager.putRandomMusic(music)
        }
    }

    fun isFavorite(musicId: String) {
        viewModelScope.launch {
            isFavoriteLiveData.value = findMusicInSQLite.find(musicId)
        }
    }

    fun setMediaItems(list: List<Music>) {
        viewModelScope.launch {
            MediaControllerManager.setMediaItems(list)
        }
    }

    fun setCurrentPosition(position: Int) {

    }
}