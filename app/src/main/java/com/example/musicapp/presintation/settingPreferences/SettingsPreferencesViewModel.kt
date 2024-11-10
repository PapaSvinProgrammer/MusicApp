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
    private val updateRecyclerDataLiveData = MutableLiveData<Boolean>()

    val getGroupAllResult: LiveData<LiveData<ArrayList<Group>>> = getGroupAllLiveData
    val countSelectedLiveData = MutableLiveData<Int>()
    val getGroupWithFilterOnGenresResult: LiveData<LiveData<ArrayList<Group>>> = getGroupGenresLiveData
    val updateRecyclerDataResult: LiveData<Boolean> = updateRecyclerDataLiveData

    val selectedArray = ArrayList<Group>()
    val selectedMap = HashMap<String, Boolean>()

    var lastDownloadArray = ArrayList<Group>()
    var lastFilter: List<Int> = ArrayList()
    var searchList: List<Group> = ArrayList()

    fun getGroup() {
        getGroupAllLiveData.value = getGroupAll.execute()
    }

    fun getGroupOnGenres(filter: List<Int>) {
        getGroupGenresLiveData.value = getGroupWithFilterOnGenres.execute(filter)
    }

    fun updateRecyclerData() {
        updateRecyclerDataLiveData.value = true
    }
}