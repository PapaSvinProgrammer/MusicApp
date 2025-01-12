package com.example.musicapp.presentation.start

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.musicapp.data.module.UserVK
import com.example.musicapp.data.module.UserYandex
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetLoginState
import com.example.musicapp.domain.usecase.http.GetUserGoogle
import com.example.musicapp.domain.usecase.http.GetUserYandex
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.launch

class StartViewModel(
    private val getLoginState: GetLoginState,
    private val saveDarkModeState: SaveDarkModeState,
    private val getDarkModeState: GetDarkModeState,
    private val saveEmail: SaveEmail,
    private val saveUserKey: SaveUserKey,
    private val saveLoginState: SaveLoginState,
    private val getUserYandex: GetUserYandex,
    private val getUserGoogle: GetUserGoogle
): ViewModel() {
    private val loginStateLiveData = MutableLiveData<Boolean>()
    private val darkModeStateLiveData = MutableLiveData<Boolean>()
    private val userYandexLiveData = MutableLiveData<UserYandex?>()
    private val userGoogleLiveData = MutableLiveData<AuthResult?>()
    private val userVkLiveData = MutableLiveData<UserVK?>()

    val loginStateResult: LiveData<Boolean> = loginStateLiveData
    val getDarkModeStateResult: LiveData<Boolean> = darkModeStateLiveData
    val userYandexResult: LiveData<UserYandex?> = userYandexLiveData
    val userGoogleResult: LiveData<AuthResult?> = userGoogleLiveData
    val userVkResult: LiveData<UserVK?> = userVkLiveData

    fun getLoginSate() {
        loginStateLiveData.value = getLoginState.execute()
    }

    fun saveDarkMode(state: Boolean) {
        saveDarkModeState.execute(state)
    }

    fun getDarkMode() {
        darkModeStateLiveData.value = getDarkModeState.execute()
    }

    fun saveEmail(email: String) {
        saveEmail.execute(email)
    }

    fun saveUserKey(userKey: String) {
        saveUserKey.execute(userKey)
    }

    fun saveLoginState(state: Boolean) {
        saveLoginState.execute(state)
    }

    fun getUserGoogle(credential: Credential) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                userGoogleLiveData.value = getUserGoogle.execute(googleIdToken.idToken)
            }
        }
    }

    fun getUserYandex(token: YandexAuthToken) {
        viewModelScope.launch {
            userYandexLiveData.value = getUserYandex.execute(token.value)
        }
    }
}