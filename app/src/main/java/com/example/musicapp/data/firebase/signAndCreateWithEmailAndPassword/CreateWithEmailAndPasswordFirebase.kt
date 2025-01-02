package com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword

import android.util.Log
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.LoginData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class CreateWithEmailAndPasswordFirebase {
    suspend fun execute(loginData: LoginData): String? {
        val auth = Firebase.auth
        var userId: String? = null

        try {
            auth.createUserWithEmailAndPassword(loginData.email, loginData.password)
                .await()
                .apply {
                    userId = auth.currentUser?.uid
                }
        }
        catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "CreateWithEmailAndPasswordFirebase - Error")
        }

        return userId
    }
}