package com.example.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPassword
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPassword
import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class SignAndCreateRepositoryImpl(
    private val signWithEmailAndPassword: SignWithEmailAndPassword,
    private val createWithEmailAndPassword: CreateWithEmailAndPassword
): SignAndCreateRepository {
    override fun signWithEmailAndPassword(data: LoginData): LiveData<String?> {
        signWithEmailAndPassword.execute(data)
        return signWithEmailAndPassword.userId
    }

    override fun createWithEmailAndPassword(data: LoginData): LiveData<String?> {
        createWithEmailAndPassword.execute(data)
        return createWithEmailAndPassword.userId
    }
}