package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetCountMusicTest {
    private val repository = mock<PlaylistRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun `correct get count music in playlist` (): Unit = runBlocking {
        val useCase = GetCountMusic(repository)
        val testId = 2L
        val testResult = flow { emit(2) }

        Mockito.`when`(repository.getCountMusicInPlaylist(testId)).thenReturn(testResult)

        val expected = 2
        var actual = -2

        useCase.getCountMusicInPlaylist(testId).take(1).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getCountMusicInPlaylist(testId)
    }

    @Test
    fun `invalid get count music in playlist` (): Unit = runBlocking {
        val useCase = GetCountMusic(repository)
        val testId = -2L
        val testResult = flow { emit(2) }

        Mockito.`when`(repository.getCountMusicInPlaylist(testId)).thenReturn(testResult)

        val expected = -1
        var actual = -2

        useCase.getCountMusicInPlaylist(testId).take(1).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getCountMusicInPlaylist(testId)
    }
}