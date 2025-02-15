package com.example.musicapp.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.module.Music
import com.example.musicapp.app.support.convertTextCount.ConvertTextCount
import com.example.musicapp.domain.usecase.room.get.GetCountDownloadMusic
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import com.example.musicapp.domain.usecase.room.get.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.domain.usecase.room.get.GetCountPlaylist
import com.example.musicapp.domain.usecase.room.get.GetMusicsFromPlaylistSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import kotlinx.coroutines.launch

private const val DEFAULT_PLAYLIST_ID = 1L
private const val AUTHOR_LIMIT = 8
private const val MUSIC_LIMIT = 12
private const val DOWNLOAD_LIMIT = 2
private const val PLAYLIST_LIMIT = 2
private const val ALBUM_LIMIT = 2

class FavoriteViewModel(
    private val getAuthorsFromSQLite: GetAuthorsFromSQLite,
    private val convertTextCount: ConvertTextCount,
    private val getPlaylistFromSQLite: GetPlaylistFromSQLite,
    private val getDownloadedMusic: GetDownloadedMusic,
    private val getCountDownloadMusic: GetCountDownloadMusic,
    private val getCountMusic: GetCountMusic,
    private val getCountPlaylist: GetCountPlaylist,
    private val getMusicsFromPlaylistSQLite: GetMusicsFromPlaylistSQLite
): ViewModel() {
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
            getMusicsFromPlaylistSQLite.getMusicsFromPlaylist(
                playlistId = DEFAULT_PLAYLIST_ID,
                limit = MUSIC_LIMIT
            ).collect {
                getMusicLiveData.value = it
            }
        }
    }

    fun getAuthor() {
        viewModelScope.launch {
            getAuthorsFromSQLite.execute(AUTHOR_LIMIT).collect {
                getAuthorLiveData.value = it
            }
        }
    }

    fun getPlaylist() {
        viewModelScope.launch {
            getPlaylistFromSQLite.getPlaylists(PLAYLIST_LIMIT).collect {
                getPlaylistLiveData.value = it
            }
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
            getCountMusic.getCountMusicInPlaylist(DEFAULT_PLAYLIST_ID).collect {
                convertCountMusicLiveData.value = convertTextCountMusic(it)
            }
        }
    }

    fun getCountPlaylist() {
        viewModelScope.launch {
            getCountPlaylist.execute().collect {
                convertCountPlaylistLiveData.value = convertTextCountPlaylist(it)
            }
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