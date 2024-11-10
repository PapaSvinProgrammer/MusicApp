package com.example.musicapp.presintation.settingPreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.usecase.getGroup.GetGroupAll
import com.example.musicapp.domain.usecase.getGroup.GetGroupWithFilterOnGenres

class SettingsPreferencesViewModel(
    private val getGroupAll: GetGroupAll,
    private val getGroupWithFilterOnGenres: GetGroupWithFilterOnGenres
): ViewModel() {
    private val getGroupLiveData = MutableLiveData<LiveData<ArrayList<Group>>>()
    private val updateRecyclerDataLiveData = MutableLiveData<Boolean>()

    val getGroupResult: LiveData<LiveData<ArrayList<Group>>> = getGroupLiveData
    val countSelectedLiveData = MutableLiveData<Int>()
    val updateRecyclerDataResult: LiveData<Boolean> = updateRecyclerDataLiveData

    val selectedArray = ArrayList<Group>()
    val selectedMap = HashMap<String, Boolean>()

    var lastDownloadArray = ArrayList<Group>()
    var lastFilter: List<Int> = ArrayList()
    var searchList: List<Group> = ArrayList()

    fun getGroup() {
        getGroupLiveData.value = getGroupAll.execute()
    }

    fun getGroupOnGenres(filter: List<Int>) {
        getGroupLiveData.value = getGroupWithFilterOnGenres.execute(filter)
    }

    fun updateRecyclerData() {
        updateRecyclerDataLiveData.value = true
    }
}