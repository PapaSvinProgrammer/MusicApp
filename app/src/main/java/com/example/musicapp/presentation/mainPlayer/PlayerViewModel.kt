package com.example.musicapp.presentation.mainPlayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.app.service.player.MediaControllerManager
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.ControlPlayer
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteMusicFromSQLite
import com.example.musicapp.app.service.video.VideoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val PLAYLIST_ID_FAVORITE = 1L

class PlayerViewModel(
    private val addMusicInSQLite: AddMusicInSQLite,
    private val deleteMusicFromSQLite: DeleteMusicFromSQLite
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var videoService: VideoService? = null
    var isSuccessVideo: LiveData<Boolean>? = null
    val isBoundVideo = MutableLiveData<Boolean>()
    var isFavorite = false

    private val controlPlayerLiveData = MutableLiveData<ControlPlayer>()
    private val statePlayerLiveData = MutableLiveData<StatePlayer>()

    val controlPlayer: LiveData<ControlPlayer> = controlPlayerLiveData
    val statePlayer: LiveData<StatePlayer> = statePlayerLiveData

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun setControlPlayer(state: ControlPlayer) {
        controlPlayerLiveData.value = state
    }

    fun seekTo(msec: Int?) {
        MediaControllerManager
            .mediaController
            .seekTo(msec?.toLong() ?: 0)
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