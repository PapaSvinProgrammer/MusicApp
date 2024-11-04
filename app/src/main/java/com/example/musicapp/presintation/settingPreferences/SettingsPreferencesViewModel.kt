package com.example.musicapp.presintation.settingPreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getGroup.GetGroupAll
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll

class SettingsPreferencesViewModel(
    private val getGroupAll: GetGroupAll
): ViewModel() {
    private val getGroupAllLiveData = MutableLiveData<LiveData<ArrayList<Group>>>()
    val getGroupAllResult: LiveData<LiveData<ArrayList<Group>>> = getGroupAllLiveData

    fun getGroup() {
        getGroupAllLiveData.value = getGroupAll.execute()
    }
}