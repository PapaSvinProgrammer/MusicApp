package com.example.musicapp.domain.usecase.getPreferences

import com.example.musicapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetDarkModeStateTest {
    private val preferencesRepository = mock<PreferencesRepository>()
    private val useCase = GetDarkModeState(preferencesRepository)

    @Test
    @DisplayName("Matching of input and output values")
    fun test() {
        val testDarkMode = flow<Boolean> { true}
        Mockito.`when`(preferencesRepository.getDarkMode()).thenReturn(testDarkMode)

        val actual = useCase.execute()

        Assertions.assertEquals(testDarkMode, actual)
    }
}