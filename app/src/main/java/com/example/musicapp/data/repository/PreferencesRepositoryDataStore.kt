package com.example.musicapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.musicapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREF_SETTING_NAME = "UserSettings"
private const val DEFAULT_RESULT = "Null"
private val FIELD_USER_KEY = stringPreferencesKey("UserKey")
private val FIELD_LOGIN_STATE = booleanPreferencesKey("LoginState")
private val FIELD_DARK_MODE = booleanPreferencesKey("DarkMode")
private val FIELD_EMAIL = stringPreferencesKey("Email")

class PreferencesRepositoryDataStore(private val context: Context): PreferencesRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_SETTING_NAME)

    override suspend fun saveUserKey(key: String): Boolean {
        context.dataStore.edit { settings ->
            settings[FIELD_USER_KEY] = key
        }

        return true
    }

    override suspend fun saveLoginState(state: Boolean): Boolean {
        context.dataStore.edit { settings ->
            settings[FIELD_LOGIN_STATE] = state
        }

        return true
    }

    override suspend fun saveDarkMode(state: Boolean): Boolean {
        context.dataStore.edit { settings ->
            settings[FIELD_DARK_MODE] = state
        }

        return true
    }

    override suspend fun saveEmail(email: String): Boolean {
        context.dataStore.edit { settings ->
            settings[FIELD_EMAIL] = email
        }

        return true
    }

    override fun getUserKey(): Flow<String> {
        return context.dataStore.data.map { settings ->
            settings[FIELD_USER_KEY] ?: DEFAULT_RESULT
        }
    }

    override fun getLoginState(): Flow<Boolean> {
        return context.dataStore.data.map { settings ->
            settings[FIELD_LOGIN_STATE] ?: false
        }
    }

    override fun getDarkMode(): Flow<Boolean> {
        return context.dataStore.data.map { settings ->
            settings[FIELD_DARK_MODE] ?: false
        }
    }

    override fun getEmail(): Flow<String> {
        return context.dataStore.data.map { settings ->
            settings[FIELD_EMAIL] ?: DEFAULT_RESULT
        }
    }
}