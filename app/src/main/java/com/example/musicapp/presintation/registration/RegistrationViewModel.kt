package com.example.musicapp.presintation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.signAndCreate.CreateAccount
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.domain.usecase.valid.EmailValid
import com.example.musicapp.domain.usecase.valid.PasswordValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val registrationLiveData = MutableLiveData<LiveData<String?>>()

    val emailValidResult: LiveData<Boolean> = emailValidLiveData
    val passwordValidResult: LiveData<Boolean> = passwordValidLiveData
    val passwordEqualsResult: LiveData<Boolean> = passwordEqualsLiveData
    val registrationResult: LiveData<LiveData<String?>> = registrationLiveData

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
        registrationLiveData.value = createAccount.execute(
            email = email,
            password = password
        )
    }

    fun saveLoginState() {
        CoroutineScope(Dispatchers.IO).launch {
            saveLoginState.execute(true)
        }
    }

    fun saveUserKey(userKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            saveUserKey.execute(userKey)
        }
    }

    fun saveEmail(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            saveEmail.execute(email)
        }
    }
}