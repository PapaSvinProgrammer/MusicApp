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

class GetCountMusicInPlaylistMusicTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetCountMusicInPlaylistAll(): Unit = runBlocking {
        val useCase = GetCountMusic(repository)
        val testResult = 3

        Mockito.`when`(repository.getCount()).thenReturn(testResult)

        val expected = 3
        val actual = useCase.getCountMusicInPlaylist()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getCount()
    }

    @Test
    fun correctGetCountMusicInPlaylistInPlaylist(): Unit = runBlocking {
        val useCase = GetCountMusic(repository)
        val testResult = 3
        val testId = 3L

        Mockito.`when`(repository.getCount(testId)).thenReturn(testResult)

        val expected = 3
        val actual = useCase.getCountMusicInPlaylist(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getCount(testId)
    }

    @Test
    fun invalidGetCountMusicInPlaylistInPlaylist(): Unit = runBlocking {
        val useCase = GetCountMusic(repository)
        val testResult = 3
        val testId = -2L

        Mockito.`when`(repository.getCount(testId)).thenReturn(testResult)

        val expected = -1
        val actual = useCase.getCountMusicInPlaylist(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getCount(testId)
    }
}