package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetLoginStateTest {
    private val repository = mock<PreferencesRepository>()
    private val useCase = GetLoginState(repository)

    @Test
    fun correctReturnData() {
        val testLoginState = flow<Boolean> { true }
        Mockito.`when`(repository.getLoginState()).thenReturn(testLoginState)

        val actual = useCase.execute()

        Assertions.assertEquals(testLoginState, actual)
    }
}