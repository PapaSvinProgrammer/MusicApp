package com.example.musicapp.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val saveDarkModeState: SaveDarkModeState,
    private val saveLoginState: SaveLoginState,
    private val getEmail: GetEmail,
    private val getDarkModeState: GetDarkModeState
): ViewModel() {
    private val _getEmail = MutableLiveData<String>()
    private val _getDarkModeState = MutableLiveData<Boolean>()

    val getEmailResult: LiveData<String> = _getEmail
    val getDarkModeStateResult: LiveData<Boolean> = _getDarkModeState

    fun saveDarkMode(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveDarkModeState.execute(state)
        }
    }

    fun saveLoginState() {
        viewModelScope.launch(Dispatchers.IO) {
            saveLoginState.execute(false)
        }
    }

    fun getEmail() {
        viewModelScope.launch {
            getEmail.execute().collect { result ->
                _getEmail.value = result
            }
        }
    }

    fun getDarkMode() {
        viewModelScope.launch {
            getDarkModeState.execute().collect { result ->
                _getDarkModeState.value = result
            }
        }
    }

}