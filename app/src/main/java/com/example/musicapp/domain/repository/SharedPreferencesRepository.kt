package com.example.musicapp.domain.repository

interface SharedPreferencesRepository {
    fun saveUserKey(key: String): Boolean
    fun saveLoginState(state: Boolean): Boolean
    fun saveDarkMode(state: Boolean): Boolean
    fun saveEmail(email: String): Boolean

    fun getUserKey(): String
    fun getLoginState(): Boolean
    fun getDarkMode(): Boolean
    fun getEmail(): String
}