package com.example.musicapp.presentation.mainPlayer

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
import com.example.musicapp.domain.state.ControlPlayer
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteMusicFromSQLite
import com.example.musicapp.domain.usecase.room.find.FindMusicInSQLite
import com.example.musicapp.app.service.video.VideoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

private const val PLAYLIST_ID_FAVORITE = 1L

class PlayerViewModel(
    private val addMusicInSQLite: AddMusicInSQLite,
    private val deleteMusicFromSQLite: DeleteMusicFromSQLite,
    private val findMusicInSQLite: FindMusicInSQLite
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var videoService: VideoService? = null
    var isSuccessVideo: LiveData<Boolean>? = null
    val isBoundVideo = MutableLiveData<Boolean>()

    var isFavorite = false

    private val controlPlayerLiveData = MutableLiveData<ControlPlayer>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()
    private val missTimeLiveData = MutableLiveData<String>()
    private val passTimeLiveData = MutableLiveData<String>()
    private val getFavoriteMusicLiveData = MutableLiveData<MusicResult?>()

    val controlPlayer: LiveData<ControlPlayer> = controlPlayerLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData
    val missTimeResult: LiveData<String> = missTimeLiveData
    val passTimeResult: LiveData<String> = passTimeLiveData
    val getFavoriteMusicResult: LiveData<MusicResult?> = getFavoriteMusicLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun setControlPlayer(state: ControlPlayer) {
        controlPlayerLiveData.value = state
    }

    fun seekTo(msec: Int?) {

    }

    @SuppressLint("SimpleDateFormat")
    fun getMissTime(current: Long?) {
        viewModelScope.launch {

        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getPassTime(current: Long) {
        viewModelScope.launch {

        }
    }

    fun getFavoriteMusic(id: String) {
        viewModelScope.launch {
            getFavoriteMusicLiveData.value = findMusicInSQLite.find(id)
        }
    }

    fun addFavoriteMusic(music: Music) {
        viewModelScope.launch(Dispatchers.IO) {
            addMusicInSQLite.execute(
                music = music,
                playlistId = PLAYLIST_ID_FAVORITE
            )
        }
    }

    fun deleteMusic(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMusicFromSQLite.execute(id)
        }
    }

    val connectionToVideoService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as VideoService.VideoBinder
            videoService = bind.getService()
            isSuccessVideo = bind.isSuccess()
            isBoundVideo.value = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBoundVideo.value = false
        }
    }
}