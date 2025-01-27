package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class SearchDownloadedLocalTest {
    private val repository = mock<DownloadMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctExecute() {
        val useCase = SearchDownloadedLocal(repository)
        val testText = "testText"
        val testResult = listOf(Music(), Music())

        Mockito.`when`(repository.search(testText)).thenReturn(testResult)

        val expected = listOf(Music(), Music())
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).search(testText)
    }

    @Test
    fun invalidExecute() {
        val useCase = SearchDownloadedLocal(repository)
        val testText = "t"
        val testResult = listOf(Music(), Music())

        Mockito.`when`(repository.search(testText)).thenReturn(testResult)

        val expected = listOf<Music>()
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).search(testText)
    }
}