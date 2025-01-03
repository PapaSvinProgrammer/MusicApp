package com.example.musicapp.domain.usecase.signAndCreate

import com.example.musicapp.domain.module.LoginData
import com.example.musicapp.domain.repository.SignAndCreateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class CreateAndSignAccountTest {
    private val signAndCreateRepository = mock<SignAndCreateRepository>()
    private val useCase = CreateAccount(signAndCreateRepository)

    @Test
    fun correctAccountProcessing() {
        val testEmail = "email@yandex.ru"
        val testPassword = "password"
        val testLoginData = LoginData(
            email = "email@yandex.ru",
            password = "password"
        )
        val testUserId = "userId"

       CoroutineScope(Dispatchers.Unconfined).launch {
            Mockito
                .`when`(
                    signAndCreateRepository.createWithEmailAndPassword(testLoginData)
                )
                .thenReturn(testUserId)

            val expected = testUserId
            val actual = useCase.execute(testEmail, testPassword)

            Assertions.assertEquals(expected, actual)
        }
    }

    @Test
    fun invalidAccountProcessing() {
        val testEmail = ""
        val testPassword = ""
        val testLoginData = LoginData(testPassword, testPassword)
        val testUserId = null

        CoroutineScope(Dispatchers.Unconfined).launch {
            Mockito
                .`when`(
                    signAndCreateRepository.createWithEmailAndPassword(testLoginData)
                )
                .thenReturn(testUserId)

            val expected = null
            val actual: String? = useCase.execute(testEmail, testPassword)

            Assertions.assertEquals(expected, actual)
        }
    }
}