package com.example.musicapp.presentation.album

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumById
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAlbumId
import com.example.musicapp.app.service.player.PlayerService
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlbumViewModel(
    private val getAlbumById: GetAlbumById,
    private val getMusicsByAlbumId: GetMusicsByAlbumId
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var playerService: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val getAlbumLiveData = MutableLiveData<Album?>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val convertYearLiveData = MutableLiveData<String>()

    val getAlbumResult: LiveData<Album?> = getAlbumLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val convertYearResult: LiveData<String> = convertYearLiveData

    fun getAlbum(id: String) {
        viewModelScope.launch {
            getAlbumLiveData.value = getAlbumById.execute(id)
        }
    }

    fun getMusic(albumId: String) {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicsByAlbumId.execute(albumId)
        }
    }

    fun convertYear(time: Timestamp?) {
        if (time == null) {
            return
        }

        val date = Date(time.seconds * 1000)
        val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        convertYearLiveData.value = simpleDateFormat.format(date)
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            playerService = binder.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}
