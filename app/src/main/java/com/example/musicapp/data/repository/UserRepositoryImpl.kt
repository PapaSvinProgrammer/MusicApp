package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.GetUserGoogleImpl
import com.example.musicapp.data.http.get.GetUserYandexImpl
import com.example.musicapp.data.module.UserVK
import com.example.musicapp.data.module.UserYandex
import com.example.musicapp.domain.repository.UserRepository
import com.google.firebase.auth.AuthResult

class UserRepositoryImpl(
    private val getUserYandexImpl: GetUserYandexImpl,
    private val getUserGoogleImpl: GetUserGoogleImpl,
    private val getUserVkImpl: GetUserVkImpl
): UserRepository {
    override suspend fun getUserYandex(token: String): UserYandex? {
        return getUserYandexImpl.execute(token)
    }

    override suspend fun getUserVk(token: String, clientId: String): UserVK? {
        return getUserVkImpl.execute(
            token = token,
            clientId = clientId
        )
    }

    override suspend fun getUserGoogle(token: String): AuthResult? {
        return getUserGoogleImpl.execute(token)
    }
}