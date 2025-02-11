package com.example.musicapp.presentation.bottomSheetMusic

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.state.ActionMusic
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.find.FindMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteDownloadMusic
import com.example.musicapp.domain.usecase.room.downloadMusic.DownloadMusic
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import com.example.musicapp.domain.usecase.room.add.AddSaveMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteSaveMusicFromSQLite
import com.example.musicapp.app.support.ConvertTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class MusicBottomSheetViewModel(
    private val findMusicInSQLite: FindMusicInSQLite,
    private val addMusicInSQLite: AddMusicInSQLite,
    private val getDownloadedMusic: GetDownloadedMusic,
    private val downloadMusic: DownloadMusic,
    private val deleteDownloadMusic: DeleteDownloadMusic,
    private val addSaveMusicInSQLite: AddSaveMusicInSQLite,
    private val deleteSaveMusicFromSQLite: DeleteSaveMusicFromSQLite
): ViewModel() {
    private val actionLiveData = MutableLiveData<ActionMusic>()
    private val isFavoriteLiveData = MutableLiveData<MusicResult>()
    private val isDownloadLiveData = MutableLiveData<Music?>()
    private val convertTimeLiveData = MutableLiveData<String>()

    val actionResult: LiveData<ActionMusic> = actionLiveData
    val isFavoriteResult: LiveData<MusicResult?> = isFavoriteLiveData
    val isDownloadResult: LiveData<Music?> = isDownloadLiveData
    val convertTimeResult: LiveData<String> = convertTimeLiveData

    fun setAction(action: ActionMusic) {
        actionLiveData.value = action
    }

    fun isFavorite(id: String) {
        viewModelScope.launch {
            isFavoriteLiveData.value = findMusicInSQLite.find(id)
        }
    }

    fun isDownload(id: String) {
        viewModelScope.launch {
            isDownloadLiveData.value = getDownloadedMusic.getDownload(id)
        }
    }

    fun like(music: Music?) {
        viewModelScope.launch(Dispatchers.IO) {
            addMusicInSQLite.execute(music)
        }
    }

    fun download(music: Music?) {
        if (music == null) {
            return
        }

        downloadMusic.execute(music)

        viewModelScope.launch(Dispatchers.IO) {
            addSaveMusicInSQLite.execute(music.id ?: "")
        }
    }

    fun delete(musicId: String) {
        deleteDownloadMusic.execute(musicId)

        viewModelScope.launch(Dispatchers.IO) {
            deleteSaveMusicFromSQLite.execute(musicId)
        }
    }

    fun playNext(music: Music?) {
        if (music == null) {
            return
        }


    }

    fun addToQueue(music: Music?) {
        if (music == null) {
            return
        }


    }

    fun convertTime(time: Int) {
        convertTimeLiveData.value = ConvertTime().convertInMinSec(time)
    }
}