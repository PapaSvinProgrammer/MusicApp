package com.example.musicapp.presintation.settings

import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.save.SaveDarkModeState
import com.example.musicapp.domain.usecase.save.SaveLoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val saveDarkModeState: SaveDarkModeState,
    private val saveLoginState: SaveLoginState
): ViewModel() {

    fun saveDarkMode(state: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            saveDarkModeState.execute(state)
        }
    }

    fun saveLoginState() {
        saveLoginState.execute(false)
    }
}