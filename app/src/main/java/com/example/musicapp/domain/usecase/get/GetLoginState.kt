package com.example.musicapp.domain.usecase.get

import com.example.musicapp.domain.repository.SharedPreferencesRepository

class GetLoginState(private val sharedPreferencesRepository: SharedPreferencesRepository) {
    fun execute(): Boolean {
        return sharedPreferencesRepository.getLoginState()
    }
}