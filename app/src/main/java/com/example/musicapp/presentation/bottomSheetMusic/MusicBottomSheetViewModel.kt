package com.example.musicapp.presentation.bottomSheetMusic

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.state.ActionMusic
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.find.FindFavoriteMusicFromSQLite
import com.example.musicapp.domain.usecase.downloadMusic.DeleteDownloadMusic
import com.example.musicapp.domain.usecase.downloadMusic.DownloadMusic
import com.example.musicapp.domain.usecase.downloadMusic.GetDownloadedMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class MusicBottomSheetViewModel(
    private val findFavoriteMusicFromSQLite: FindFavoriteMusicFromSQLite,
    private val addMusicInSQLite: AddMusicInSQLite,
    private val getDownloadedMusic: GetDownloadedMusic,
    private val downloadMusic: DownloadMusic,
    private val deleteDownloadMusic: DeleteDownloadMusic
): ViewModel() {
    private val actionLiveData = MutableLiveData<ActionMusic>()
    private val isFavoriteLiveData = MutableLiveData<MusicResult>()
    private val isDownloadLiveData = MutableLiveData<SaveMusic?>()

    val actionResult: LiveData<ActionMusic> = actionLiveData
    val isFavoriteResult: LiveData<MusicResult?> = isFavoriteLiveData
    val isDownloadResult: LiveData<SaveMusic?> = isDownloadLiveData

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
            isDownloadLiveData.value = getDownloadedMusic.getDownload(id)
        }
    }

    fun like(music: Music?) {
        viewModelScope.launch(Dispatchers.IO) {
            addMusicInSQLite.execute(music)
        }
    }

    fun download(musicId: String, url: String) {
        downloadMusic.execute(
            musicId = musicId,
            url = url
        )
    }

    fun delete(musicId: String) {
        deleteDownloadMusic.execute(musicId)
    }
}