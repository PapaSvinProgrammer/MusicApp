package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetLoginState(private val preferencesRepository: PreferencesRepository) {
    fun execute(): Flow<Boolean> {
        return preferencesRepository.getLoginState()
    }
}