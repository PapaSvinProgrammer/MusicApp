package com.example.musicapp.domain.usecase.search.searchFirebase

import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.SearchRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class SearchAllTest {
    private val repository = mock<SearchRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctExecute(): Unit = runBlocking {
        val useCase = SearchAll(repository)
        val testText = "testText"
        val searchData = SearchData(testText)
        val testResult = listOf(Music(), Music())

        Mockito.`when`(repository.searchAll(searchData)).thenReturn(testResult)

        val expected = listOf(Music(), Music())
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).searchAll(searchData)
    }

    @Test
    fun invalidExecute(): Unit = runBlocking {
        val useCase = SearchAll(repository)
        val testText = "t"
        val searchData = SearchData(testText)
        val testResult = listOf(Music(), Music())

        Mockito.`when`(repository.searchAll(searchData)).thenReturn(testResult)

        val expected = listOf<Music>()
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).searchAll(searchData)
    }
}