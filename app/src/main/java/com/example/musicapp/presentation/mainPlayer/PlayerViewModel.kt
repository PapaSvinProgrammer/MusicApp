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
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.state.ControlPlayer
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteMusicFromSQLite
import com.example.musicapp.domain.usecase.room.FindFavoriteMusicFromSQLite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

private const val PLAYLIST_ID_FAVORITE = 1L

class PlayerViewModel(
    private val addMusicInSQLite: AddMusicInSQLite,
    private val deleteMusicFromSQLite: DeleteMusicFromSQLite,
    private val findFavoriteMusicFromSQLite: FindFavoriteMusicFromSQLite
): ViewModel() {
    lateinit var durationLiveData: LiveData<Long>
    lateinit var maxDurationLiveData: LiveData<Long>
    lateinit var isPlay: LiveData<Boolean>
    lateinit var isRepeat: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    lateinit var bufferedPosition: LiveData<Long>
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService

    val isBound = MutableLiveData<Boolean>()
    var isFavorite = false
    var isDownloaded = false
    var isCreated = false

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
    var lastPosition = 0

    fun setStatePlayer(state: StatePlayer) {
        statePlayerLiveData.value = state
    }

    fun setControlPlayer(state: ControlPlayer) {
        controlPlayerLiveData.value = state
    }

    fun seekTo(msec: Int?) {
        if (msec != null) servicePlayer.seekTo(msec)
    }

    @SuppressLint("SimpleDateFormat")
    fun getMissTime(current: Long?) {
        viewModelScope.launch {
            if (current == null) return@launch
            val result = (maxDurationLiveData.value ?: 0) - current

            val simpleDateFormat = SimpleDateFormat("m:ss")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = result.toLong()

            missTimeLiveData.value = simpleDateFormat.format(calendar.time)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getPassTime(current: Long) {
        viewModelScope.launch {
            val simpleDateFormat = SimpleDateFormat("m:ss")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = current.toLong()

            passTimeLiveData.value = simpleDateFormat.format(calendar.time)
        }
    }

    fun getMusicById(id: String) {
        viewModelScope.launch {
            getFavoriteMusicLiveData.value = findFavoriteMusicFromSQLite.execute(id)
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

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            maxDurationLiveData = bind.getMaxDuration()
            durationLiveData = bind.getCurrentDuration()
            isPlay = bind.isPlay()
            isRepeat = bind.isRepeat()
            currentPosition = bind.getCurrentPosition()
            bufferedPosition = bind.getBufferedPosition()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}