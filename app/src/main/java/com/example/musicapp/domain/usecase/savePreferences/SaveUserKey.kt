package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class SaveUserKey(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(userKey: String): Boolean {
        if (userKey.isEmpty()) {
            return false
        }

        return sharedPreferencesRepository.saveUserKey(userKey)
    }
}