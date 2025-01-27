package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetTimePlaylistTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetTime(): Unit = runBlocking {
        val useCase = GetTimePlaylist(repository)
        val testResult = 24L
        val testId = 2L

        Mockito.`when`(repository.getTime(testId)).thenReturn(testResult)

        val expected = 24L
        val actual = useCase.getTime(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getTime(testId)
    }

    @Test
    fun invalidGetTime(): Unit = runBlocking {
        val useCase = GetTimePlaylist(repository)
        val testResult = 24L
        val testId = -2L

        Mockito.`when`(repository.getTime(testId)).thenReturn(testResult)

        val expected = -1L
        val actual = useCase.getTime(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getTime(testId)
    }
}