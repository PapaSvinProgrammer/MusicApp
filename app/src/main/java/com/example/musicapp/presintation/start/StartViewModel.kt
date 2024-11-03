package com.example.musicapp.presintation.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.get.GetDarkModeState
import com.example.musicapp.domain.usecase.get.GetLoginState

class StartViewModel(
    private val getDarkModeState: GetDarkModeState,
    private val getLoginState: GetLoginState
): ViewModel() {
    val darkModeStateLiveData = MutableLiveData<Boolean>()
    val loginStateLiveData = MutableLiveData<Boolean>()

    fun getDarkModeState() {
        darkModeStateLiveData.value = getDarkModeState.execute()
    }

    fun getLoginSate() {
        loginStateLiveData.value = getLoginState.execute()
    }
}