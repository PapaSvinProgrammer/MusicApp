package com.example.musicapp.domain.usecase.valid

class EmailValid {
    fun execute(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}