package com.example.musicapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.signAndCreate.SignInAccount
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.domain.usecase.valid.EmailValid
import com.example.musicapp.domain.usecase.valid.PasswordValid
import com.example.musicapp.domain.usecase.valid.ValidState
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
    private val loginLiveData = MutableLiveData<String?>()
    private val permissionForLoginLiveData = MutableLiveData<Int>()

    val emailValidResult: LiveData<Boolean> = emailValidLiveData
    val passwordValidResult: LiveData<Boolean> = passwordValidLiveData
    val loginResult: LiveData<String?> = loginLiveData
    val permissionForLoginResult: LiveData<Int> = permissionForLoginLiveData

    fun isValidEmail(email: String) {
        emailValidLiveData.value = emailValid.execute(email)
    }

    fun isValidPassword(password: String) {
        passwordValidLiveData.value = passwordValid.execute(password)
    }

    fun loginInAccount(email: String, password: String) {
        viewModelScope.launch {
            loginLiveData.value = signInAccount.execute(
                email = email,
                password = password
            )
        }
    }

    fun saveUserKey(userKey: String) {
        saveUserKey.execute(userKey)
    }

    fun saveLoginState() {
        saveLoginState.execute(true)
    }

    fun saveEmail(email: String) {
        saveEmail.execute(email)
    }

    fun setPermissionForLogin(validState: ValidState) {
        when (validState) {
            ValidState.ERROR -> permissionForLoginLiveData.value = 0

            ValidState.VALID -> {
                permissionForLoginLiveData.value = (permissionForLoginLiveData.value ?: 0) + 1
            }
        }
    }
}