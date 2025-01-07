package com.example.musicapp.presentation.bottomSheetMusicInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.MusicInfo
import com.example.musicapp.domain.usecase.getAnother.GetMusicInfo
import kotlinx.coroutines.launch

class MusicInfoBottomSheetViewModel(
    private val getMusicInfo: GetMusicInfo
): ViewModel() {
    private val getMusicInfoLiveData = MutableLiveData<MusicInfo?>()
    val getMusicInfoResult: LiveData<MusicInfo?> = getMusicInfoLiveData

    fun getInfo(id: String) {
        viewModelScope.launch {
            getMusicInfoLiveData.value = getMusicInfo.execute(id)
        }
    }
}