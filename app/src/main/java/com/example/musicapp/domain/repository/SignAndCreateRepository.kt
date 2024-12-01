package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.LoginData

interface SignAndCreateRepository {
    suspend fun signWithEmailAndPassword(data: LoginData): String?
    suspend fun createWithEmailAndPassword(data: LoginData): String?
}