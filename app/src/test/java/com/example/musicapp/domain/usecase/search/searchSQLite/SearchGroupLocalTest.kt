package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class SearchGroupLocalTest {
    private val repository = mock<MusicLiteRepository>()
    private val authorEntity = mock<AuthorEntity>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
        Mockito.reset(authorEntity)
    }

    @Test
    fun correctExecute(): Unit = runBlocking {
        val useCase = SearchGroupLocal(repository)
        val testText = "testText"
        val testResult = listOf(authorEntity, authorEntity)

        Mockito.`when`(repository.searchAuthor(testText)).thenReturn(testResult)

        val expected = listOf(authorEntity, authorEntity)
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).searchAuthor(testText)
    }

    @Test
    fun invalidExecute(): Unit = runBlocking {
        val useCase = SearchGroupLocal(repository)
        val testText = "t"
        val testResult = listOf(authorEntity, authorEntity)

        Mockito.`when`(repository.searchAuthor(testText)).thenReturn(testResult)

        val expected = listOf<AuthorEntity>()
        val actual = useCase.execute(testText)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).searchAuthor(testText)
    }
}