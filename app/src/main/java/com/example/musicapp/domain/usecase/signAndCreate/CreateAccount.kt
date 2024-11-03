package com.example.musicapp.domain.usecase.signAndCreate

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class CreateAccount(private val signAndCreateRepository: SignAndCreateRepository) {
    fun execute(email: String, password: String): LiveData<String?> {
        return signAndCreateRepository.createWithEmailAndPassword(
            data = LoginData(
                email = email,
                password = password
            )
        )
    }
}