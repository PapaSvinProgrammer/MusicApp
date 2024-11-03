package com.example.musicapp.domain.usecase.valid

class PasswordValid {
    fun execute(password: String): Boolean {
        return password.length >= 6
    }
}