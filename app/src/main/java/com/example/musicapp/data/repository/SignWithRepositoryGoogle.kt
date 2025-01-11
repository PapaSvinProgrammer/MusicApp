package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithGoogleImpl
import com.example.musicapp.domain.repository.SignWithRepository
import com.google.firebase.auth.AuthResult

class SignWithRepositoryGoogle(
    private val signWithGoogleImpl: SignWithGoogleImpl
): SignWithRepository {
    override suspend fun sign(tokenId: String): AuthResult? {
        return signWithGoogleImpl.execute(tokenId)
    }
}