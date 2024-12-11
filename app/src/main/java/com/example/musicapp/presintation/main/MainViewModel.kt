package com.example.musicapp.presintation.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.player.state.StatePlayer
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetEmail
import com.example.musicapp.domain.usecase.getPreferences.GetUserKey
import com.example.musicapp.domain.usecase.room.AddPlaylistInSQLite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val PLAYLIST_FAVORITE_NAME = "Мне нравится"
private const val PLAYLIST_FAVORITE_URL = "https://i.pinimg.com/736x/3f/53/e0/3f53e0e8e2da84c69f814e0d8f629e8f.jpg"

class MainViewModel(
    private val getDarkModeState: GetDarkModeState,
    private val getEmail: GetEmail,
    private val getUserKey: GetUserKey,
    private val getMusicAll: GetMusicAll,
    private val addPlaylistInSQLite: AddPlaylistInSQLite
): ViewModel() {
    lateinit var durationLiveData: LiveData<Int>
    lateinit var maxDurationLiveData: LiveData<Int>
    lateinit var isPlayService: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()
    var darkModeResult: Boolean = false
    var emailResult: String? = null
    var userKeyResult: String? = null
    var lastPosition = 0
    var lastDownloadArray = ArrayList<Music>()

    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()

    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun getDarkMode() {
        darkModeResult = getDarkModeState.execute()
    }

    fun getEmail() {
        emailResult = getEmail.execute()
    }

    fun getUserKey() {
        userKeyResult = getUserKey.execute()
    }

    fun getMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicAll.execute()
        }
    }

    fun addFavoritePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistInSQLite.execute(
                name = PLAYLIST_FAVORITE_NAME,
                image = PLAYLIST_FAVORITE_URL
            )
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            maxDurationLiveData = binder.getMaxDuration()
            durationLiveData = binder.getCurrentDuration()
            currentPosition = binder.getCurrentPosition()
            isPlayService = binder.isPlay()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}