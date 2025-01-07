package com.example.musicapp.presentation.bottomSheetMusicText

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.MusicText
import com.example.musicapp.domain.usecase.getAnother.GetMusicText
import kotlinx.coroutines.launch

class MusicTextBottomSheetViewModel(
    private val getMusicText: GetMusicText
): ViewModel() {
    private val musicTextLiveData = MutableLiveData<MusicText?>()
    val musicTextResult: LiveData<MusicText?> = musicTextLiveData

    fun getText(id: String) {
        viewModelScope.launch {
            musicTextLiveData.value = getMusicText.getTextById(id)
        }
    }
}