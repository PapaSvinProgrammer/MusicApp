package com.example.musicapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveUserKey(key: String): Boolean
    suspend fun saveLoginState(state: Boolean): Boolean
    suspend fun saveDarkMode(state: Boolean): Boolean
    suspend fun saveEmail(email: String): Boolean

    fun getUserKey(): Flow<String>
    fun getLoginState(): Flow<Boolean>
    fun getDarkMode(): Flow<Boolean>
    fun getEmail(): Flow<String>
}