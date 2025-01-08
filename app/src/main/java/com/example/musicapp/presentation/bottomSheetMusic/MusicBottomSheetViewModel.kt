package com.example.musicapp.presentation.bottomSheetMusic

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.ActionMusic
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.add.AddSaveMusicInSQLite
import com.example.musicapp.domain.usecase.room.find.FindFavoriteMusicFromSQLite
import com.example.musicapp.domain.usecase.room.find.FindSaveMusicFromSQLite
import com.example.musicapp.service.audioDownloader.AudioDownloadHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class MusicBottomSheetViewModel(
    private val findFavoriteMusicFromSQLite: FindFavoriteMusicFromSQLite,
    private val findSaveMusicFromSQLite: FindSaveMusicFromSQLite,
    private val addMusicInSQLite: AddMusicInSQLite,
    private val audioDownloadHelper: AudioDownloadHelper,
    private val addSaveMusicInSQLite: AddSaveMusicInSQLite
): ViewModel() {
    private val actionLiveData = MutableLiveData<ActionMusic>()
    private val isFavoriteLiveData = MutableLiveData<MusicResult>()
    private val isDownloadLiveData = MutableLiveData<SaveMusicEntity?>()

    val actionResult: LiveData<ActionMusic> = actionLiveData
    val isFavoriteResult: LiveData<MusicResult?> = isFavoriteLiveData
    val isDownloadResult: LiveData<SaveMusicEntity?> = isDownloadLiveData

    fun setAction(action: ActionMusic) {
        actionLiveData.value = action
    }

    fun isFavorite(id: String) {
        viewModelScope.launch {
            isFavoriteLiveData.value = findFavoriteMusicFromSQLite.execute(id)
        }
    }

    fun isDownload(id: String) {
        viewModelScope.launch {
            isDownloadLiveData.value = findSaveMusicFromSQLite.execute(id)
        }
    }

    fun like(music: Music?) {
        viewModelScope.launch(Dispatchers.IO) {
            addMusicInSQLite.execute(music)
        }
    }

    fun download(musicId: String, url: String) {
        if (musicId.isEmpty() || url.isEmpty()) {
            return
        }

        viewModelScope.launch {
            addSaveMusicInSQLite.execute(musicId)
        }

        audioDownloadHelper.download(
            musicId = musicId,
            url = url
        )
    }
}