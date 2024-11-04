package com.example.musicapp.presintation.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState

class StartViewModel(
    private val getLoginState: GetLoginState,
    private val saveDarkModeState: SaveDarkModeState,
    private val getDarkModeState: GetDarkModeState
): ViewModel() {
    private val loginStateLiveData = MutableLiveData<Boolean>()
    private val darkModeStateLiveData = MutableLiveData<Boolean>()

    val loginStateResult: LiveData<Boolean> = loginStateLiveData
    val getDarkModeStateResult: LiveData<Boolean> = darkModeStateLiveData

    fun getLoginSate() {
        loginStateLiveData.value = getLoginState.execute()
    }

    fun saveDarkMode(state: Boolean) {
        saveDarkModeState.execute(state)
    }

    fun getDarkMode() {
        darkModeStateLiveData.value = getDarkModeState.execute()
    }
}