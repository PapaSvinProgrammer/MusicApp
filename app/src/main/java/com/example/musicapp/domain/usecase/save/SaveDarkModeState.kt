package com.example.musicapp.domain.usecase.save

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class SaveDarkModeState(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(state: Boolean): Boolean {
        return sharedPreferencesRepository.saveDarkMode(state)
    }
}