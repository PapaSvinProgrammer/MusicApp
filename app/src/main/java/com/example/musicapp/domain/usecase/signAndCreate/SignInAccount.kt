package com.example.musicapp.domain.usecase.signAndCreate

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class SignInAccount(private val signAndCreateRepository: SignAndCreateRepository) {
    fun execute(email: String, password: String): LiveData<String?> {
        return signAndCreateRepository.signWithEmailAndPassword(
            data = LoginData(
                email = email,
                password = password
            )
        )
    }
}