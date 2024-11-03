package com.example.musicapp.domain.usecase.save

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class SaveLoginState(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(state: Boolean): Boolean {
        return sharedPreferencesRepository.saveLoginState(state)
    }
}