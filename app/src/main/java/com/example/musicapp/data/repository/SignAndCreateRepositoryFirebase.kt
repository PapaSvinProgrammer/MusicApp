package com.example.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPasswordFirebase
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPasswordFirebase
import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository

class SignAndCreateRepositoryFirebase(
    private val signWithEmailAndPasswordFirebase: SignWithEmailAndPasswordFirebase,
    private val createWithEmailAndPasswordFirebase: CreateWithEmailAndPasswordFirebase
): SignAndCreateRepository {
    override fun signWithEmailAndPassword(data: LoginData): LiveData<String?> {
        signWithEmailAndPasswordFirebase.execute(data)
        return signWithEmailAndPasswordFirebase.userId
    }

    override fun createWithEmailAndPassword(data: LoginData): LiveData<String?> {
        createWithEmailAndPasswordFirebase.execute(data)
        return createWithEmailAndPasswordFirebase.userId
    }
}