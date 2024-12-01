package com.example.musicapp.domain.usecase.signAndCreate

import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class CreateAccount(private val signAndCreateRepository: SignAndCreateRepository) {
    suspend fun execute(email: String, password: String): String? {
        return signAndCreateRepository.createWithEmailAndPassword(
            data = LoginData(
                email = email,
                password = password
            )
        )
    }
}