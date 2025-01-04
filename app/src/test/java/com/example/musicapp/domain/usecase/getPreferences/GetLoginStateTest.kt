package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetLoginStateTest {
    private val repository = mock<SharedPreferencesRepository>()
    private val useCase = GetLoginState(repository)

    @Test
    fun correctReturnData() {
        val testLoginState = true
        Mockito.`when`(repository.getLoginState()).thenReturn(testLoginState)

        val expected = true
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}