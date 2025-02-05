package com.example.musicapp.presentation.settingPreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.usecase.getGroup.GetGroup
import com.example.musicapp.domain.usecase.getGroup.GetGroupWithFilterOnGenres
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchGroup
import kotlinx.coroutines.launch

class SettingsPreferencesViewModel(
    private val getGroup: GetGroup,
    private val getGroupWithFilterOnGenres: GetGroupWithFilterOnGenres,
    private val searchGroup: SearchGroup
): ViewModel() {
    private val getGroupLiveData = MutableLiveData<List<Group>>()
    private val countSelectedLiveData = MutableLiveData<Int>()
    private val searchLiveData = MutableLiveData<List<Group>>()

    val getGroupResult: LiveData<List<Group>> = getGroupLiveData
    val countSelectedResult: LiveData<Int> = countSelectedLiveData
    val searchResult: LiveData<List<Group>> = searchLiveData

    val selectedArray = ArrayList<Group>()

    fun addSelectedItem(item: Group) {
        selectedArray.add(item)
        addCountSelected()
    }

    fun addCountSelected() {
        countSelectedLiveData.value = (countSelectedLiveData.value ?: 0) + 1
    }

    fun removeCountSelected() {
        countSelectedLiveData.value = (countSelectedLiveData.value ?: 0) - 1
    }

    fun removeSelectedItem(item: Group) {
        selectedArray.remove(item)
        removeCountSelected()
    }

    fun getGroup() {
        viewModelScope.launch {
            getGroupLiveData.value = getGroup.getGroupAll()
        }
    }

    fun getGroupOnGenres(filter: List<Int>) {
        viewModelScope.launch {
            getGroupLiveData.value = getGroupWithFilterOnGenres.execute(filter)
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            searchLiveData.value = searchGroup.execute(text)
        }
    }
}