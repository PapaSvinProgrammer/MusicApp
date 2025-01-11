package com.example.musicapp.domain.repository

import com.google.firebase.auth.AuthResult

interface SignWithRepository {
    suspend fun sign(tokenId: String): AuthResult?
}