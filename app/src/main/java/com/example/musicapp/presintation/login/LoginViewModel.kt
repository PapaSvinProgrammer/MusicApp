package com.example.musicapp.presintation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.signAndCreate.SignInAccount
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.domain.usecase.valid.EmailValid
import com.example.musicapp.domain.usecase.valid.PasswordValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val emailValid: EmailValid,
    private val passwordValid: PasswordValid,
    private val signInAccount: SignInAccount,
    private val saveUserKey: SaveUserKey,
    private val saveLoginState: SaveLoginState,
    private val saveEmail: SaveEmail
): ViewModel() {
    private val emailValidLiveData = MutableLiveData<Boolean>()
    private val passwordValidLiveData = MutableLiveData<Boolean>()
    private val loginLiveData = MutableLiveData<LiveData<String?>>()

    val emailValidResult: LiveData<Boolean> = emailValidLiveData
    val passwordValidResult: LiveData<Boolean> = passwordValidLiveData
    val loginResult: LiveData<LiveData<String?>> = loginLiveData

    fun isValidEmail(email: String) {
        emailValidLiveData.value = emailValid.execute(email)
    }

    fun isValidPassword(password: String) {
        passwordValidLiveData.value = passwordValid.execute(password)
    }

    fun loginInAccount(email: String, password: String) {
        loginLiveData.value = signInAccount.execute(
            email = email,
            password = password
        )
    }

    fun saveUserKey(userKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            saveUserKey.execute(userKey)
        }
    }

    fun saveLoginState() {
        CoroutineScope(Dispatchers.IO).launch {
            saveLoginState.execute(true)
        }
    }

    fun saveEmail(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            saveEmail.execute(email)
        }
    }
}