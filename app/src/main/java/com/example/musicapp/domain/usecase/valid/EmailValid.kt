package com.example.musicapp.domain.usecase.valid

import androidx.core.util.PatternsCompat

class EmailValid {
    fun execute(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        else {
            return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}