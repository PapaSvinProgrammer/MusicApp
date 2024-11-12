package com.example.musicapp.presintation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll

class HomeViewModel(
    private val getMusicAll: GetMusicAll
): ViewModel() {
    private val getMusicLiveData = MutableLiveData<LiveData<ArrayList<Music>>>()
    val getMusicResult: LiveData<LiveData<ArrayList<Music>>> = getMusicLiveData

    fun getMusic() {
        getMusicLiveData.value = getMusicAll.execute()
    }

    fun getMusicWithFilter() {
        //TODO
    }
}