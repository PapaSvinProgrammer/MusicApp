package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.SharedPreferencesRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetUsrKeyTest {
    private val repository = mock<SharedPreferencesRepository>()
    private val useCase = GetUserKey(repository)

    @Test
    fun correctReturnData() {
        val testUserKey = "userKey"
        Mockito.`when`(repository.getUserKey()).thenReturn(testUserKey)

        val expected = "userKey"
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}