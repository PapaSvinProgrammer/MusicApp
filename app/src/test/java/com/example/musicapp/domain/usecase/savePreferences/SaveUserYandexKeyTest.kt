package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class SaveUserYandexKeyTest {
    private val repository = mock<SharedPreferencesRepository>()

    @AfterEach
    fun reset() {
        Mockito.reset(repository)
    }

    @Test
    fun correctSave() {
        val useCase = SaveUserKey(repository)
        val testResult = true
        val testUserKey = "userKeyExample"

        Mockito.`when`(repository.saveUserKey(testUserKey)).thenReturn(testResult)

        val expected = true
        val actual = useCase.execute(testUserKey)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidSave() {
        val useCase = SaveUserKey(repository)
        val testResult = false
        val testUserKey = ""

        Mockito.`when`(repository.saveUserKey(testUserKey)).thenReturn(testResult)

        val expected = false
        val actual = useCase.execute(testUserKey)

        Assertions.assertEquals(expected, actual)
    }
}