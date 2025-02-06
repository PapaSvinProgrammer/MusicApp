package com.example.musicapp.presentation.start

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.module.UserVk
import com.example.musicapp.data.module.UserYandex
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetLoginState
import com.example.musicapp.domain.usecase.http.GetUserGoogle
import com.example.musicapp.domain.usecase.http.GetUserYandex
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.presentation.main.MainActivity
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val EMAIL_VK_SCOPE = "email"
private const val PHONE_VK_SCOPE = "phone_number"

class StartViewModel(
    private val getLoginState: GetLoginState,
    private val saveDarkModeState: SaveDarkModeState,
    private val getDarkModeState: GetDarkModeState,
    private val saveEmail: SaveEmail,
    private val saveUserKey: SaveUserKey,
    private val saveLoginState: SaveLoginState,
    private val getUserYandex: GetUserYandex,
    private val getUserGoogle: GetUserGoogle,
    private val googleAuthView: GoogleAuthView
): ViewModel() {
    private val userVk = UserVk(null, null)
    private val vkidAuthCallback = object: VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            onSuccessVk(accessToken)
        }

        override fun onFail(fail: VKIDAuthFail) {
            onFailureVk(fail)
        }
    }
    private val vkidRefreshTokenCallback = object: VKIDRefreshTokenCallback {
        override fun onFail(fail: VKIDRefreshTokenFail) {
            vkRefreshTokenFailLiveData.value = fail
        }

        override fun onSuccess(token: AccessToken) {
            onSuccessVk(token)
        }
    }
    private val vkidGetUserCallback = object: VKIDGetUserCallback {
        override fun onFail(fail: VKIDGetUserFail) {
            refreshToken()
        }

        override fun onSuccess(user: VKIDUser) {
            userVk.userData = user
            userVkLiveData.value = userVk
        }
    }

    private val loginStateLiveData = MutableLiveData<Boolean>()
    private val darkModeStateLiveData = MutableLiveData<Boolean>()
    private val userYandexLiveData = MutableLiveData<UserYandex?>()
    private val userGoogleLiveData = MutableLiveData<AuthResult?>()
    private val userVkLiveData = MutableLiveData<UserVk>()
    private val vkAuthFailLiveData = MutableLiveData<VKIDAuthFail>()
    private val vkRefreshTokenFailLiveData = MutableLiveData<VKIDRefreshTokenFail>()
    private val googleAuthLiveData = MutableLiveData<GetCredentialResponse>()

    val loginStateResult: LiveData<Boolean> = loginStateLiveData
    val getDarkModeStateResult: LiveData<Boolean> = darkModeStateLiveData
    val userYandexResult: LiveData<UserYandex?> = userYandexLiveData
    val userGoogleResult: LiveData<AuthResult?> = userGoogleLiveData
    val userVkResult: LiveData<UserVk> = userVkLiveData
    val vkAuthFailResult: LiveData<VKIDAuthFail> = vkAuthFailLiveData
    val vkRefreshTokenFailResult: LiveData<VKIDRefreshTokenFail> = vkRefreshTokenFailLiveData
    val googleAuthResult: LiveData<GetCredentialResponse> = googleAuthLiveData

    fun getLoginSate() {
        viewModelScope.launch {
            getLoginState.execute().collect {
                loginStateLiveData.value = it
            }
        }
    }

    fun saveDarkMode(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveDarkModeState.execute(state)
        }
    }

    fun getDarkMode() {
        viewModelScope.launch {
            getDarkModeState.execute().collect {
                darkModeStateLiveData.value = it
            }
        }
    }

    fun saveEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveEmail.execute(email)
        }
    }

    fun saveUserKey(userKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserKey.execute(userKey)
        }
    }

    fun saveLoginState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveLoginState.execute(state)
        }
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

    fun authVk() {
        viewModelScope.launch {
            VKID.instance.authorize(
                callback = vkidAuthCallback,
                params = VKIDAuthParams {
                    scopes = setOf(EMAIL_VK_SCOPE, PHONE_VK_SCOPE)
                }
            )
        }
    }

    fun authGoogle(requireActivity: FragmentActivity) {
        viewModelScope.launch {
            googleAuthLiveData.value = googleAuthView.executeCredentialManager(requireActivity)
        }
    }

    private fun onFailureVk(fail: VKIDAuthFail) {
        when (fail) {
            is VKIDAuthFail.Canceled -> {
                vkAuthFailLiveData.value = fail
            }
            else -> {
                refreshToken()
            }
        }
    }

    private fun onSuccessVk(token: AccessToken) {
        userVk.userId = "vk-${token.userID}"

        viewModelScope.launch {
            VKID.instance.getUserData(vkidGetUserCallback)
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            VKID.instance.refreshToken(vkidRefreshTokenCallback)
        }
    }
}