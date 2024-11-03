package com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.LoginData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateWithEmailAndPassword {
    val userId = MutableLiveData<String?>()

    fun execute(loginData: LoginData) {
        val auth = Firebase.auth

        CoroutineScope(Dispatchers.Main).launch {
            auth.createUserWithEmailAndPassword(loginData.email, loginData.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userId.value = auth.currentUser?.uid
                    } else {
                        userId.value = null
                    }
                }
        }
    }
}