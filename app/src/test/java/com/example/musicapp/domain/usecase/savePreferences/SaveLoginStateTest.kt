package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class SaveLoginStateTest {
    private val repository = mock<PreferencesRepository>()

    @AfterEach
    fun reset() {
        Mockito.reset(repository)
    }

    @Test
    fun correctSave(): Unit = runBlocking {
        val testResult = true
        val useCase = SaveLoginState(repository)

        Mockito.`when`(repository.saveLoginState(any())).thenReturn(testResult)

        val expected = true
        val actual = useCase.execute(true)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidSave(): Unit = runBlocking {
        val testResult = false
        val useCase = SaveLoginState(repository)

        Mockito.`when`(repository.saveLoginState(any())).thenReturn(testResult)

        val expected = false
        val actual = useCase.execute(true)

        Assertions.assertEquals(expected, actual)
    }
}