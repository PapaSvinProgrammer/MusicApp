package com.example.musicapp.presintation.favorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.usecase.convert.ConvertTextCountMusic
import com.example.musicapp.domain.usecase.room.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.room.GetMusicFromSQLite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val MUSIC_LIMIT = 12
private const val AUTHOR_LIMIT = 8

class FavoriteViewModel(
    private val getMusicFromSQLite: GetMusicFromSQLite,
    private val getAuthorsFromSQLite: GetAuthorsFromSQLite,
    private val convertTextCountMusic: ConvertTextCountMusic
): ViewModel() {
    lateinit var durationLiveData: LiveData<Int>
    lateinit var maxDurationLiveData: LiveData<Int>
    lateinit var isPlay: LiveData<Boolean>
    lateinit var currentPosition: LiveData<Int>
    @SuppressLint("StaticFieldLeak")
    lateinit var servicePlayer: PlayerService
    val isBound = MutableLiveData<Boolean>()
    var listSize = 0

    private val getMusicLiveData = MutableLiveData<List<MusicResult?>>()
    private val getAuthorLiveData = MutableLiveData<List<AuthorEntity?>>()
    private val convertCountMusicLiveData = MutableLiveData<String>()

    val getMusicResult: LiveData<List<MusicResult?>> = getMusicLiveData
    val getAuthorResult: LiveData<List<AuthorEntity?>> = getAuthorLiveData
    val convertCountMusicResult: LiveData<String> = convertCountMusicLiveData

    fun getMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicFromSQLite.execute(MUSIC_LIMIT)
        }
    }

    fun getAuthor() {
        viewModelScope.launch {
            getAuthorLiveData.value = getAuthorsFromSQLite.execute(AUTHOR_LIMIT)
        }
    }

    fun getPlaylists() {
        //TODO
    }

    fun convertTextCountMusic(count: Int) {
        convertCountMusicLiveData.value = convertTextCountMusic.execute(count)
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            maxDurationLiveData = bind.getMaxDuration()
            durationLiveData = bind.getCurrentDuration()
            isPlay = bind.isPlay()
            currentPosition = bind.getCurrentPosition()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}