package com.example.musicapp.presintation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.domain.usecase.signAndCreate.CreateAccount
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.domain.usecase.valid.EmailValid
import com.example.musicapp.domain.usecase.valid.PasswordValid
import com.example.musicapp.domain.usecase.valid.ValidState
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val emailValid: EmailValid,
    private val passwordValid: PasswordValid,
    private val createAccount: CreateAccount,
    private val saveUserKey: SaveUserKey,
    private val saveLoginState: SaveLoginState,
    private val saveEmail: SaveEmail
): ViewModel() {
    private val emailValidLiveData = MutableLiveData<Boolean>()
    private val passwordValidLiveData = MutableLiveData<Boolean>()
    private val passwordEqualsLiveData = MutableLiveData<Boolean>()
    private val registrationLiveData = MutableLiveData<String?>()
    private val permissionForRegistrationLiveData = MutableLiveData<Int>()

    val emailValidResult: LiveData<Boolean> = emailValidLiveData
    val passwordValidResult: LiveData<Boolean> = passwordValidLiveData
    val passwordEqualsResult: LiveData<Boolean> = passwordEqualsLiveData
    val registrationResult: LiveData<String?> = registrationLiveData
    val permissionForRegistrationResult: LiveData<Int> = permissionForRegistrationLiveData

    fun isEmailValid(email: String) {
        emailValidLiveData.value = emailValid.execute(email)
    }

    fun isPasswordValid(password: String) {
        passwordValidLiveData.value = passwordValid.execute(password)
    }

    fun passwordEqual(password1: String, password2: String) {
        passwordEqualsLiveData.value = password1 == password2
    }

    fun registrationAccount(email: String, password: String) {
        viewModelScope.launch {
            registrationLiveData.value = createAccount.execute(
                email = email,
                password = password
            )
        }
    }

    fun saveLoginState() {
        saveLoginState.execute(true)
    }

    fun saveUserKey(userKey: String) {
        saveUserKey.execute(userKey)
    }

    fun saveEmail(email: String) {
        saveEmail.execute(email)
    }

    fun setPermissionForRegistration(validState: ValidState) {
        when (validState) {
            ValidState.ERROR -> permissionForRegistrationLiveData.value = 0

            ValidState.VALID -> {
                permissionForRegistrationLiveData.value = (permissionForRegistrationLiveData.value ?: 0) + 1
            }
        }
    }
}