package com.example.musicapp.domain.usecase.valid

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PasswordValidTest {
    private val passwordValid = PasswordValid()

    @Test
    fun correctPasswordValidProcessing() {
        val expected = true
        val actual = passwordValid.execute("123456")

        Assertions.assertEquals(expected,actual)
    }

    @Test
    fun notCorrectPasswordValidProcessing() {
        val expected = false
        val actual = passwordValid.execute("123")

        Assertions.assertEquals(expected, actual)
    }
}