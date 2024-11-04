package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class SaveDarkModeState(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(state: Boolean): Boolean {
        return sharedPreferencesRepository.saveDarkMode(state)
    }
}