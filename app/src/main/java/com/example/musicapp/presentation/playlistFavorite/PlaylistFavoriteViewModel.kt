package com.example.musicapp.presentation.playlistFavorite

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.app.support.convertTextCount.ConvertTextCount
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.app.support.ConvertTime
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
            getTimePlaylist.getTime(DEFAULT_PLAYLIST_ID).collect {
                _timePlaylist.value = ConvertTime().convertInMinSec(it)
            }
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
}