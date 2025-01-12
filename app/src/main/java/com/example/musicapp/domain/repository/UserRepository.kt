package com.example.musicapp.domain.repository

import com.example.musicapp.data.module.UserVK
import com.example.musicapp.data.module.UserYandex
import com.google.firebase.auth.AuthResult

interface UserRepository {
    suspend fun getUserYandex(token: String): UserYandex?
    suspend fun getUserVk(token: String, clientId: String): UserVK?
    suspend fun getUserGoogle(token: String): AuthResult?
}