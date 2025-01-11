package com.example.musicapp.presentation.start

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.domain.usecase.signAndCreate.SignWithGoogle
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartViewModel(
    private val getLoginState: GetLoginState,
    private val saveDarkModeState: SaveDarkModeState,
    private val getDarkModeState: GetDarkModeState,
    private val saveEmail: SaveEmail,
    private val saveUserKey: SaveUserKey,
    private val saveLoginState: SaveLoginState,
    private val signWithGoogle: SignWithGoogle
): ViewModel() {
    private val loginStateLiveData = MutableLiveData<Boolean>()
    private val darkModeStateLiveData = MutableLiveData<Boolean>()

    val loginStateResult: LiveData<Boolean> = loginStateLiveData
    val getDarkModeStateResult: LiveData<Boolean> = darkModeStateLiveData

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

    fun signWithGoogle(credential: Credential) {
        CoroutineScope(Dispatchers.IO).launch {
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                signWithGoogle.execute(googleIdToken.idToken)
            }
        }
    }
}