package com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword

import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.LoginData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignWithEmailAndPasswordFirebase {
    val userId = MutableLiveData<String?>()

    fun execute(data: LoginData) {
        val auth = Firebase.auth

        CoroutineScope(Dispatchers.Main).launch {
            auth.signInWithEmailAndPassword(data.email, data.password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        userId.value = auth.currentUser?.uid
                    }
                    else {
                        userId.value = null
                    }
                }
        }
    }
}