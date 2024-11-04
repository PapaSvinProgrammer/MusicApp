package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class GetDarkModeState(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(): Boolean {
        return sharedPreferencesRepository.getDarkMode()
    }
}