package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetMusicFromSQLiteTest {
    private val repository = mock<MusicLiteRepository>()
    private val musicResult = mock<MusicResult>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
        Mockito.reset(musicResult)
    }

    @Test
    fun `correct get all`(): Unit = runBlocking {
        val useCase = GetMusicFromSQLite(repository)
        val testResult = flowOf(listOf(musicResult, musicResult))

        Mockito.`when`(repository.getAllMusic()).thenReturn(testResult)

        val expected = listOf(musicResult, musicResult)
        var actual = listOf<MusicResult>()

        useCase.getAllMusic().collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAllMusic()
    }

    @Test
    fun `correct get limit`(): Unit = runBlocking {
        val useCase = GetMusicFromSQLite(repository)
        val testResult = flowOf(listOf(musicResult, musicResult))
        val testLimit = 2

        Mockito.`when`(repository.getMusicLimit(testLimit)).thenReturn(testResult)

        val expected = listOf(musicResult, musicResult)
        var actual = listOf<MusicResult>()

        useCase.getAllMusic(testLimit).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getMusicLimit(testLimit)
    }

    @Test
    fun `invalid get limit`(): Unit = runBlocking {
        val useCase = GetMusicFromSQLite(repository)
        val testLimit = -1

        val expected = listOf<MusicResult>()
        var actual: List<MusicResult>? = null

        useCase.getAllMusic(testLimit).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getMusicLimit(testLimit)
    }
}