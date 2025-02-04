package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.PreferencesRepository

class SaveLoginState(private val preferencesRepository: PreferencesRepository) {
    suspend fun execute(state: Boolean): Boolean {
        return preferencesRepository.saveLoginState(state)
    }
}