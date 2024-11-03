package com.example.musicapp.data.repository

import android.content.Context
import com.example.musicapp.domain.repository.SharedPreferencesRepository

const val PREF_SETTING_NAME = "UserSettings"
const val KEY_USER_KEY = "UserKey"
const val KEY_LOGIN_STATE = "LoginState"
const val KEY_DARK_MODE = "DarkMode"
const val KEY_EMAIL = "Email"

class SharedPreferencesRepositoryImpl(context: Context): SharedPreferencesRepository {
    private val sharedPreferences = context.getSharedPreferences(PREF_SETTING_NAME, Context.MODE_PRIVATE)

    override fun saveUserKey(key: String): Boolean {
        sharedPreferences.edit().putString(KEY_USER_KEY, key).apply()
        return true
    }

    override fun saveLoginState(state: Boolean): Boolean {
        sharedPreferences.edit().putBoolean(KEY_LOGIN_STATE, state).apply()
        return true
    }

    override fun saveDarkMode(state: Boolean): Boolean {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, state).apply()
        return true
    }

    override fun saveEmail(email: String): Boolean {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply()
        return true
    }

    override fun getUserKey(): String {
        return sharedPreferences.getString(KEY_USER_KEY, "") ?: ""
    }

    override fun getLoginState(): Boolean {
        return sharedPreferences.getBoolean(KEY_LOGIN_STATE, false)
    }

    override fun getDarkMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }

    override fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    }
}