package com.example.musicapp.presentation.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAlbumId
import com.example.musicapp.domain.usecase.getAlbum.GetAlbum
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlbumViewModel(
    private val getAlbum: GetAlbum,
    private val getMusicsByAlbumId: GetMusicsByAlbumId
): ViewModel() {
    private val getAlbumLiveData = MutableLiveData<Album?>()
    private val getMusicLiveData = MutableLiveData<List<Music>>()
    private val convertYearLiveData = MutableLiveData<String>()

    val getAlbumResult: LiveData<Album?> = getAlbumLiveData
    val getMusicResult: LiveData<List<Music>> = getMusicLiveData
    val convertYearResult: LiveData<String> = convertYearLiveData

    fun getAlbum(id: String) {
        viewModelScope.launch {
            getAlbumLiveData.value = getAlbum.getAlbumById(id)
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
}
