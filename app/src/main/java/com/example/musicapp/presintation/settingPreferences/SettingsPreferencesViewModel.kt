package com.example.musicapp.presintation.settingPreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.getGroup.GetGroupAll
import com.example.musicapp.domain.usecase.getGroup.GetGroupWithFilterOnGenres
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll

class SettingsPreferencesViewModel(
    private val getGroupAll: GetGroupAll,
    private val getGroupWithFilterOnGenres: GetGroupWithFilterOnGenres
): ViewModel() {
    private val getGroupAllLiveData = MutableLiveData<LiveData<ArrayList<Group>>>()
    private val getGroupGenresLiveData = MutableLiveData<LiveData<ArrayList<Group>>>()

    val getGroupAllResult: LiveData<LiveData<ArrayList<Group>>> = getGroupAllLiveData
    val countSelectedLiveData = MutableLiveData<Int>()
    val getGroupWithFilterOnGenresResult: LiveData<LiveData<ArrayList<Group>>> = getGroupGenresLiveData

    val selectedArray = ArrayList<Group>()
    var lastDownloadArray = ArrayList<Group>()

    fun getGroup() {
        getGroupAllLiveData.value = getGroupAll.execute()
    }

    fun getGroupOnGenres(filter: List<Int>) {
        getGroupGenresLiveData.value = getGroupWithFilterOnGenres.execute(filter)
    }
}