package com.example.musicapp.domain.usecase.http

import com.example.musicapp.data.module.UserYandex
import com.example.musicapp.domain.repository.UserRepository

class GetUserYandex(
    private val userRepository: UserRepository
) {
    suspend fun execute(token: String): UserYandex? {
        if (token.isEmpty()) {
            return null
        }

        return userRepository.getUserYandex(token)
    }
}