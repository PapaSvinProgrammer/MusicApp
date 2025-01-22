package com.example.musicapp.presentation.bottomSheetAuthorInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.module.GroupInfo
import com.example.musicapp.domain.usecase.getAnother.GetGroupInfo
import kotlinx.coroutines.launch

class AuthorInfoViewModel(
    private val getGroupInfo: GetGroupInfo
): ViewModel() {
    private val infoLiveData = MutableLiveData<GroupInfo?>()
    val groupResult: LiveData<GroupInfo?> = infoLiveData

    fun getInfo(groupId: String) {
        viewModelScope.launch {
            infoLiveData.value = getGroupInfo.execute(groupId)
        }
    }
}