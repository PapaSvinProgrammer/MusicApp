package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetTimePlaylistTest {
    private val repository = mock<PlaylistRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun `correct get time`(): Unit = runBlocking {
        val useCase = GetTimePlaylist(repository)
        val testId = 2L
        val testResult = flowOf(232)

        Mockito.`when`(repository.getTimePlaylist(testId)).thenReturn(testResult)

        val expected = 232
        var actual = 0

        useCase.getTime(testId).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getTimePlaylist(testId)
    }

    @Test
    fun `invalid get time`(): Unit = runBlocking {
        val useCase = GetTimePlaylist(repository)
        val testId = -2L
        val testResult = flowOf(232)

        Mockito.`when`(repository.getTimePlaylist(testId)).thenReturn(testResult)

        val expected = -1
        var actual = 0

        useCase.getTime(testId).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getTimePlaylist(testId)
    }
}