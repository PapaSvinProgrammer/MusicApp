package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetMusicAllTest {
    private val repository = mock<MusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetMusicAll(repository)
        val testResult = listOf(Music(), Music())

        Mockito.`when`(repository.getMusicAll()).thenReturn(testResult)

        val expected = listOf(Music(), Music())
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}