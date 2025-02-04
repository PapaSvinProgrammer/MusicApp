package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.PreferencesRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetEmailTest {
    private val repository = mock<PreferencesRepository>()
    private val useCase = GetEmail(repository)

    @Test
    fun correctEmailReturn() {
        val testEmail = "test@yandex.ru"
        Mockito.`when`(repository.getEmail()).thenReturn(testEmail)

        val actual = useCase.execute()

        Assertions.assertEquals(testEmail, actual)
    }
}