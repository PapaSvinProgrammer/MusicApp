package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetRandomMusicTest {
    private val repository = mock<MusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetMusicSingle(): Unit = runBlocking {
        val useCase = GetRandomMusic(repository)
        val testResult = listOf(Music())
        val testLimit = 1L

        Mockito.`when`(repository.getRandomMusic(testLimit)).thenReturn(testResult)

        val expected = Music()
        val actual = useCase.getMusicSingle()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctGetMusics(): Unit = runBlocking {
        val useCase = GetRandomMusic(repository)
        val testResult = listOf(Music(), Music(), Music())
        val testLimit = 3L

        Mockito.`when`(repository.getRandomMusic(testLimit)).thenReturn(testResult)

        val expected = listOf(Music(), Music(), Music())
        val actual = useCase.getMusics(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getRandomMusic(testLimit)
    }

    @Test
    fun invalidGetMusicSingle(): Unit = runBlocking {
        val useCase = GetRandomMusic(repository)
        val testResult = listOf<Music>()
        val testLimit = 1L

        Mockito.`when`(repository.getRandomMusic(testLimit)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getMusicSingle()

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidGetMusics(): Unit = runBlocking {
        val useCase = GetRandomMusic(repository)
        val testResult = listOf<Music>()
        val testLimit = 0L

        Mockito.`when`(repository.getRandomMusic(testLimit)).thenReturn(testResult)

        val expected = listOf<Music>()
        val actual = useCase.getMusics(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getRandomMusic(testLimit)
    }
}