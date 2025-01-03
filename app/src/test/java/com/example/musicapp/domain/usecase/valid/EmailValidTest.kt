package com.example.musicapp.domain.usecase.valid

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EmailValidTest {
    private val emailValid = EmailValid()

    @Test
    fun correctEmailAddressProcessing() {
        val expected = true
        val actual1 = emailValid.execute("ura@yandex.ru")
        val actual2 = emailValid.execute("u12312@gmail.com")
        val actual3 = emailValid.execute("ura@ya.u")

        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
        Assertions.assertEquals(expected, actual3)
    }

    @Test
    fun invalidEmailAddressProcessing() {
        val expected = false
        val actual1 = emailValid.execute("ura@yandex.")
        val actual2 = emailValid.execute("@gmail.com")
        val actual3 = emailValid.execute("ura@ФЫвфывфы.u")
        val actual4 = emailValid.execute("")
        val actual5 = emailValid.execute("asdsadas")

        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
        Assertions.assertEquals(expected, actual3)
        Assertions.assertEquals(expected, actual4)
        Assertions.assertEquals(expected, actual5)
    }
}