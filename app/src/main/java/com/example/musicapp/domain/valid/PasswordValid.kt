package com.example.musicapp.domain.valid

class PasswordValid {
    fun execute(password: String): Boolean {
        return password.length >= 6
    }
}