package com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword

import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class SignWithGoogleImpl {
    suspend fun execute(tokenId: String): AuthResult? {
        val firebaseCredential = GoogleAuthProvider.getCredential(tokenId, null)
        return Firebase.auth.signInWithCredential(firebaseCredential).await()
    }
}