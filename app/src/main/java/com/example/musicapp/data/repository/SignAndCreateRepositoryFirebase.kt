package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPasswordFirebase
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPasswordFirebase
import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class SignAndCreateRepositoryFirebase(
    private val signWithEmailAndPasswordFirebase: SignWithEmailAndPasswordFirebase,
    private val createWithEmailAndPasswordFirebase: CreateWithEmailAndPasswordFirebase
): SignAndCreateRepository {
    override suspend fun signWithEmailAndPassword(data: LoginData): String? {
        return signWithEmailAndPasswordFirebase.execute(data)
    }

    override suspend fun createWithEmailAndPassword(data: LoginData): String? {
        return createWithEmailAndPasswordFirebase.execute(data)
    }
}