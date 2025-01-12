package com.example.musicapp.domain.usecase.http

import com.example.musicapp.domain.repository.UserRepository
import com.google.firebase.auth.AuthResult

class GetUserGoogle(
    private val userRepository: UserRepository
) {
    suspend fun execute(token: String): AuthResult? {
        if (token.isEmpty()) {
            return null
        }

        return userRepository.getUserGoogle(token)
    }
}