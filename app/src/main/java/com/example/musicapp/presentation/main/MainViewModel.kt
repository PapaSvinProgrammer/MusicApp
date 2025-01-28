package com.example.musicapp.presentation.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.domain.state.StatePlayer
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
    var durationLiveData: LiveData<Long>? = null
    var maxDurationLiveData: LiveData<Long>? = null
    var musicList: LiveData<List<Music>>? = null
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    var darkModeResult: Boolean = false
    var countMusicList = 0

    private val startDownloadLiveData = MutableLiveData<Boolean>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()
    private val isFavoriteLiveData = MutableLiveData<MusicResult?>()

    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val startDownloadResult: LiveData<Boolean> = startDownloadLiveData
    val isFavoriteResult: LiveData<MusicResult?> = isFavoriteLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun getDarkMode() {
        darkModeResult = getDarkModeState.execute()
    }

    fun setStartState(state: Boolean) {
        startDownloadLiveData.value = state
    }

    fun deleteMusic(musicId: String) {
        viewModelScope.launch {
            deleteMusicFromSQLite.execute(musicId)
        }
    }

    fun addMusic(music: Music) {
        viewModelScope.launch {
            addMusicInSQLite.execute(music)
        }
    }

    fun getRandomMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getRandomMusic.getMusics(DEFAULT_COUNT_MUSIC)
        }
    }

    fun addRandomMusic() {
        viewModelScope.launch {
            val music = getRandomMusic.getMusicSingle()

            if (music != null) {
                servicePlayer?.addMusic(music)
            }
        }
    }

    fun isFavorite(musicId: String) {
        viewModelScope.launch {
            isFavoriteLiveData.value = findMusicInSQLite.find(musicId)
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            maxDurationLiveData = binder.getMaxDuration()
            durationLiveData = binder.getCurrentDuration()
            musicList = binder.getMusicList()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}