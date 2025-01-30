package com.example.musicapp.presentation.playlistFavorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.app.support.convertTextCount.ConvertTextCount
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.domain.usecase.room.get.GetMusicFromSQLite
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.domain.usecase.room.get.GetTimePlaylist
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchMusicLocal
import kotlinx.coroutines.launch

private const val DEFAULT_PLAYLIST_ID = 1L

class PlaylistFavoriteViewModel(
    getMusicFromSQLite: GetMusicFromSQLite,
    private val getCountMusic: GetCountMusic,
    private val convertTextCount: ConvertTextCount,
    private val getTimePlaylist: GetTimePlaylist,
    private val searchMusicLocal: SearchMusicLocal
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val countTextMusicLiveData = MutableLiveData<String>()
    private val timePlaylistLiveData = MutableLiveData<String>()
    private val searchLiveData = MutableLiveData<List<MusicResult?>>()

    val countTextMusicResult: LiveData<String> = countTextMusicLiveData
    val timePlaylistResult: LiveData<String> = timePlaylistLiveData
    val searchResult: LiveData<List<MusicResult?>> = searchLiveData

    val getMusicResult = getMusicFromSQLite.getAllMusicFromPlaylist(DEFAULT_PLAYLIST_ID).asLiveData()

    fun getCount() {
        viewModelScope.launch {
            convertMusicText(
                count = getCountMusic.getCount(DEFAULT_PLAYLIST_ID)
            )
        }
    }

    @SuppressLint("DefaultLocale")
    fun getTime() {
        viewModelScope.launch {
            val time = getTimePlaylist.getTime(1L)
            val localTime: String

            if (time > 86400) {
                localTime = String.format(
                    "%d:%02d:%02d",
                    time / 86400,
                    (time / 3600) % 24,
                    (time % 3600) / 60
                )
            }
            else {
                localTime = String.format(
                    "%d:%02d",
                    time / 3600,
                    (time % 3600) / 60
                )
            }

            timePlaylistLiveData.value = localTime + convertTextCount.convertTime((time / 3600 % 24).toInt())
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchMusicLocal.execute(text)
        }
    }

    private fun convertMusicText(count: Int) {
        countTextMusicLiveData.value = count.toString() + convertTextCount.convertMusic(count)
    }

    val connectionToPlayerService = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerBinder
            servicePlayer = binder.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound.value = false
        }
    }
}