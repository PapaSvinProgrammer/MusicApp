package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class SearchMusicLocalTest {
    private val repository = mock<PlaylistRepository>()
    private val playlistResult = mock<PlaylistResult>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
        Mockito.reset(playlistResult)
    }

    @Test
    fun correctExecute(): Unit = runBlocking {
        val useCase = SearchPlaylistLocal(repository)
        val testText = "testText"
        val testResult = listOf(playlistResult, playlistResult)

        Mockito.`when`(repository.search(testText)).thenReturn(testResult)

        val expected = listOf(playlistResult, playlistResult)
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).search(testText)
    }

    @Test
    fun invalidExecute(): Unit = runBlocking {
        val useCase = SearchPlaylistLocal(repository)
        val testText = "t"
        val testResult = listOf(playlistResult, playlistResult)

        Mockito.`when`(repository.search(testText)).thenReturn(testResult)

        val expected = listOf<PlaylistResult>()
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).search(testText)
    }
}