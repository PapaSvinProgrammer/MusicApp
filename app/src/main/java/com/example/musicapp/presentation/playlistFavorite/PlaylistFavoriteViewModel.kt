package com.example.musicapp.presentation.playlistFavorite

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.app.support.convertTextCount.ConvertTextCount
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.app.service.player.PlayerService
import com.example.musicapp.domain.usecase.room.get.GetMusicsFromPlaylistSQLite
import com.example.musicapp.domain.usecase.room.get.GetTimePlaylist
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchMusicLocal
import kotlinx.coroutines.launch

private const val DEFAULT_PLAYLIST_ID = 1L

class PlaylistFavoriteViewModel(
    private val getCountMusic: GetCountMusic,
    private val convertTextCount: ConvertTextCount,
    private val getTimePlaylist: GetTimePlaylist,
    private val searchMusicLocal: SearchMusicLocal,
    private val getMusicsFromPlaylistSQLite: GetMusicsFromPlaylistSQLite
): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var servicePlayer: PlayerService? = null
    val isBound = MutableLiveData<Boolean>()

    private val _countTextMusic = MutableLiveData<String>()
    private val _timePlaylist = MutableLiveData<String>()
    private val _search = MutableLiveData<List<MusicResult?>>()
    private val _musics = MutableLiveData<List<MusicResult>>()

    val countTextMusic: LiveData<String> = _countTextMusic
    val timePlaylist: LiveData<String> = _timePlaylist
    val search: LiveData<List<MusicResult?>> = _search
    val musics: LiveData<List<MusicResult>> = _musics

    fun getMusics() {
        viewModelScope.launch {
            getMusicsFromPlaylistSQLite.getMusicsFromPlaylist(DEFAULT_PLAYLIST_ID).collect {
                _musics.value = it
            }
        }
    }

    fun getCount() {
        viewModelScope.launch {
            getCountMusic.getCountMusicInPlaylist(DEFAULT_PLAYLIST_ID).collect {
                convertMusicText(it)
            }
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

            _timePlaylist.value = localTime + convertTextCount.convertTime((time / 3600 % 24).toInt())
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            _search.value = searchMusicLocal.execute(text)
        }
    }

    private fun convertMusicText(count: Int) {
        _countTextMusic.value = count.toString() + convertTextCount.convertMusic(count)
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