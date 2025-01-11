package com.example.musicapp.domain.usecase.signAndCreate

import com.example.musicapp.domain.repository.SignWithRepository
import com.google.firebase.auth.AuthResult

class SignWithGoogle(
    private val signWithRepository: SignWithRepository
) {
    suspend fun execute(tokenId: String): AuthResult? {
        if (tokenId.isEmpty()) {
            return null
        }

        return signWithRepository.sign(tokenId)
    }
}