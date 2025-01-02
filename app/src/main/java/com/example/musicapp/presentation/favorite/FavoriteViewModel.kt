package com.example.musicapp.presentation.favorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.PlayerService
import com.example.musicapp.domain.usecase.convert.ConvertTextCount
import com.example.musicapp.domain.usecase.room.get.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetMusicFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import kotlinx.coroutines.launch

private const val MUSIC_LIMIT = 12
private const val AUTHOR_LIMIT = 8

class FavoriteViewModel(
    private val getMusicFromSQLite: GetMusicFromSQLite,
    private val getAuthorsFromSQLite: GetAuthorsFromSQLite,
    private val convertTextCount: ConvertTextCount,
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite
): ViewModel() {
    var isPlay: LiveData<Boolean>? = null
    var currentPosition: LiveData<Int>? = null
    var currentObject: LiveData<Music>? = null
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    var musicListSize = 0
    var playlistSize = 0
    var albumListSize = 0
    var downloadedListSize = 0

    private val getMusicLiveData = MutableLiveData<List<MusicResult?>>()
    private val getAuthorLiveData = MutableLiveData<List<AuthorEntity?>>()
    private val convertCountMusicLiveData = MutableLiveData<String>()
    private val convertCountPlaylistLiveData = MutableLiveData<String>()

    val getMusicResult: LiveData<List<MusicResult?>> = getMusicLiveData
    val getAuthorResult: LiveData<List<AuthorEntity?>> = getAuthorLiveData
    val convertCountMusicResult: LiveData<String> = convertCountMusicLiveData
    val convertCountPlaylistResult: LiveData<String> = convertCountPlaylistLiveData

    val getPlaylistResult: LiveData<List<PlaylistEntity?>> = getPlaylistFromSQLite.getOnlyPlaylists().asLiveData()

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

    fun convertTextCountMusic(count: Int) {
        convertCountMusicLiveData.value = convertTextCount.convertMusic(count)
    }

    fun convertTextCountPlaylist(count: Int) {
        convertCountPlaylistLiveData.value = convertTextCount.convertPlaylist(count)
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            isPlay = bind.isPlay()
            currentPosition = bind.getCurrentPosition()
            currentObject = bind.getCurrentObject()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}