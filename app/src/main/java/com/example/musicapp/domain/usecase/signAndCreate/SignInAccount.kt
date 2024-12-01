package com.example.musicapp.domain.usecase.signAndCreate

import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class SignInAccount(private val signAndCreateRepository: SignAndCreateRepository) {
    suspend fun execute(email: String, password: String): String? {
        return signAndCreateRepository.signWithEmailAndPassword(
            data = LoginData(
                email = email,
                password = password
            )
        )
    }
}