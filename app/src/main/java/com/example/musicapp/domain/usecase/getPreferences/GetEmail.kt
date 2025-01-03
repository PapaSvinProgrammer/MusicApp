package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class GetEmail(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(): String {
        return sharedPreferencesRepository.getEmail()
    }
}