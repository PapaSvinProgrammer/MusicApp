package com.example.musicapp.domain.usecase.save

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class SaveUserKey(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(userKey: String): Boolean {
        return sharedPreferencesRepository.saveUserKey(userKey)
    }
}