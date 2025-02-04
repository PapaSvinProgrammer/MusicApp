package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.PreferencesRepository

class SaveUserKey(private val preferencesRepository: PreferencesRepository) {
    suspend fun execute(userKey: String): Boolean {
        if (userKey.isEmpty()) {
            return false
        }

        return preferencesRepository.saveUserKey(userKey)
    }
}