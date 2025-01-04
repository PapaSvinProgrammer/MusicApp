package com.example.musicapp.domain.usecase.savePreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class SaveEmailTest {
    private val repository = mock<SharedPreferencesRepository>()

    @AfterEach
    fun reset() {
        Mockito.reset(repository)
    }

    @Test
    fun correctSave() {
        val useCase = SaveEmail(repository)
        val testResult = true

        Mockito.`when`(repository.saveEmail(any())).thenReturn(testResult)

        val expected = true
        val actual = useCase.execute("example")

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidSave() {
        val useCase = SaveEmail(repository)
        val testResult = false

        Mockito.`when`(repository.saveEmail(any())).thenReturn(testResult)

        val expected = false
        val actual = useCase.execute("")

        Assertions.assertEquals(expected, actual)
    }
}