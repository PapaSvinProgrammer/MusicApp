package com.example.musicapp.presintation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.valid.EmailValid
import com.example.musicapp.domain.valid.PasswordValid

class LoginViewModel(
    private val emailValid: EmailValid,
    private val passwordValid: PasswordValid
): ViewModel() {
    val emailValidLiveData = MutableLiveData<Boolean>()
    val passwordValidLiveData = MutableLiveData<Boolean>()

    fun isValidEmail(email: String) {
        emailValidLiveData.value = emailValid.execute(email)
    }

    fun isValidPassword(password: String) {
        passwordValidLiveData.value = passwordValid.execute(password)
    }
}