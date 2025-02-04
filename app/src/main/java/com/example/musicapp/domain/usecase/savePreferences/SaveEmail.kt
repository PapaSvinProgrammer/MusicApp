package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.PreferencesRepository

class SaveEmail(private val preferencesRepository: PreferencesRepository) {
    suspend fun execute(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }

        return preferencesRepository.saveEmail(email)
    }
}