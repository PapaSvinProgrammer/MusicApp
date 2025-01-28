package com.example.musicapp.presentation.favorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService
import com.example.musicapp.support.convertTextCount.ConvertTextCount
import com.example.musicapp.domain.usecase.room.get.GetCountDownloadMusic
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import com.example.musicapp.domain.usecase.room.get.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.domain.usecase.room.get.GetCountPlaylist
import com.example.musicapp.domain.usecase.room.get.GetMusicFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import kotlinx.coroutines.launch

private const val DEFAULT_PLAYLIST_ID = 1L
private const val AUTHOR_LIMIT = 8
private const val MUSIC_LIMIT = 12
private const val DOWNLOAD_LIMIT = 2
private const val PLAYLIST_LIMIT = 2
private const val ALBUM_LIMIT = 2

class FavoriteViewModel(
    private val getMusicFromSQLite: GetMusicFromSQLite,
    private val getAuthorsFromSQLite: GetAuthorsFromSQLite,
    private val convertTextCount: ConvertTextCount,
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val getDownloadedMusic: GetDownloadedMusic,
    private val getCountDownloadMusic: GetCountDownloadMusic,
    private val getCountMusic: GetCountMusic,
    private val getCountPlaylist: GetCountPlaylist
): ViewModel() {
    var isPlay: LiveData<Boolean>? = null
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val getMusicLiveData = MutableLiveData<List<MusicResult>>()
    private val getAuthorLiveData = MutableLiveData<List<AuthorEntity>>()
    private val convertCountMusicLiveData = MutableLiveData<String>()
    private val convertCountPlaylistLiveData = MutableLiveData<String>()
    private val convertDownloadedLiveData = MutableLiveData<String>()
    private val getDownloadedMusicLiveData = MutableLiveData<List<Music>>()
    private val getPlaylistLiveData = MutableLiveData<List<PlaylistEntity>>()

    val getMusicResult: LiveData<List<MusicResult>> = getMusicLiveData
    val getAuthorResult: LiveData<List<AuthorEntity>> = getAuthorLiveData
    val convertCountMusicResult: LiveData<String> = convertCountMusicLiveData
    val convertCountPlaylistResult: LiveData<String> = convertCountPlaylistLiveData
    val convertDownloadedResult: LiveData<String> = convertDownloadedLiveData
    val getPlaylistResult: LiveData<List<PlaylistEntity>> = getPlaylistLiveData
    val getDownloadedMusicResult: LiveData<List<Music>> = getDownloadedMusicLiveData

    fun getMusic() {
        viewModelScope.launch {
            getMusicLiveData.value = getMusicFromSQLite.getAllMusicFromPlaylist(1)
        }
    }

    fun getAuthor() {
        viewModelScope.launch {
            getAuthorLiveData.value = getAuthorsFromSQLite.execute(AUTHOR_LIMIT)
        }
    }

    fun getPlaylist() {
        viewModelScope.launch {
            getPlaylistLiveData.value = getPlaylistFromSQLite.getOnlyPlaylists(PLAYLIST_LIMIT)
        }
    }

    fun getDownloadedMusic() {
        viewModelScope.launch {
            getDownloadedMusicLiveData.value = getDownloadedMusic.getDownloadsLimit(DOWNLOAD_LIMIT)
        }
    }

    fun getCountDownloadedMusic() {
        viewModelScope.launch {
            convertDownloadedLiveData.value =
                convertTextCountDownloadedMusic(getCountDownloadMusic.execute())
        }
    }

    fun getCountMusic() {
        viewModelScope.launch {
            convertCountMusicLiveData.value =
                convertTextCountMusic(getCountMusic.getCount(DEFAULT_PLAYLIST_ID))
        }
    }

    fun getCountPlaylist() {
        viewModelScope.launch {
            convertCountPlaylistLiveData.value =
                convertTextCountPlaylist(getCountPlaylist.execute())
        }
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val bind = service as PlayerService.PlayerBinder
            servicePlayer = bind.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }

    private fun convertTextCountMusic(count: Int): String {
        return count.toString() + convertTextCount.convertMusic(count)
    }

    private fun convertTextCountPlaylist(count: Int): String {
        return count.toString() + convertTextCount.convertPlaylist(count)
    }

    private fun convertTextCountDownloadedMusic(count: Int): String {
        return count.toString() + convertTextCount.convertMusic(count)
    }
}