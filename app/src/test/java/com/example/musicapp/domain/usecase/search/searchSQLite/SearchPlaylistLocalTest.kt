package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class SearchPlaylistLocalTest {
    private val repository = mock<MusicLiteRepository>()
    private val musicResult = mock<MusicResult>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
        Mockito.reset(musicResult)
    }

    @Test
    fun correctExecute(): Unit = runBlocking {
        val useCase = SearchMusicLocal(repository)
        val testText = "testText"
        val testResult = listOf(musicResult, musicResult)

        Mockito.`when`(repository.searchMusic(testText)).thenReturn(testResult)

        val expected = listOf(musicResult, musicResult)
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).searchMusic(testText)
    }

    @Test
    fun invalidExecute(): Unit = runBlocking {
        val useCase = SearchMusicLocal(repository)
        val testText = "t"
        val testResult = listOf(musicResult, musicResult)

        Mockito.`when`(repository.searchMusic(testText)).thenReturn(testResult)

        val expected = listOf<MusicResult>()
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).searchMusic(testText)
    }
}