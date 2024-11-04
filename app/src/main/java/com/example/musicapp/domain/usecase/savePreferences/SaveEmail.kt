package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class SaveEmail(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(email: String): Boolean {
        return sharedPreferencesRepository.saveEmail(email)
    }
}