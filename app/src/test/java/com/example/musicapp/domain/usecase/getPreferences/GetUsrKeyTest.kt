package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetUsrKeyTest {
    private val repository = mock<PreferencesRepository>()
    private val useCase = GetUserKey(repository)

    @Test
    fun correctReturnData() {
        val testUserKey = flow<String> { "userKey" }
        Mockito.`when`(repository.getUserKey()).thenReturn(testUserKey)

        val actual = useCase.execute()

        Assertions.assertEquals(testUserKey, actual)
    }
}