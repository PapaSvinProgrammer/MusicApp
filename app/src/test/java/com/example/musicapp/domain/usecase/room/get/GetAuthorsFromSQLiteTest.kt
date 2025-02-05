package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetAuthorsFromSQLiteTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun `correct get all`(): Unit = runTest {
        val useCase = GetAuthorsFromSQLite(repository)
        val testResult = flow<List<AuthorEntity>> { generateItems() }

        Mockito.`when`(repository.getAllAuthor()).thenReturn(testResult)

        var expected = listOf<AuthorEntity>()
        var actual = listOf<AuthorEntity>()

        testResult.take(1).collect {
            expected = it
        }

        useCase.execute().take(1).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAllAuthor()
    }

    @Test
    fun `correct get limit`(): Unit = runBlocking {
        val useCase = GetAuthorsFromSQLite(repository)
        val testResult = flow { emit(generateItems()) }
        val testLimit = 2

        Mockito.`when`(
            repository.getAuthorLimit(
                limit = testLimit
            )
        ).thenReturn(testResult)

        val expected = generateItems()
        var actual = listOf<AuthorEntity>()

        useCase.execute(testLimit).take(1).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAuthorLimit(testLimit)
    }

    @Test
    fun `invalid get limit`(): Unit = runBlocking {
        val useCase = GetAuthorsFromSQLite(repository)
        val testResult = flow<List<AuthorEntity>> { generateItems() }
        val testLimit = -7

        Mockito.`when`(
            repository.getAuthorLimit(
                limit = testLimit
            )
        ).thenReturn(testResult)

        val expected = listOf<AuthorEntity>()
        var actual = listOf<AuthorEntity>()

        useCase.execute(testLimit).take(1).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getAuthorLimit(testLimit)
    }

    private fun generateItems(): List<AuthorEntity> {
        return listOf(
            AuthorEntity(
                id = 123L,
                firebaseId = "firebaseIdTest1",
                name = "nameTest1",
                imageUrl = ""
            ),
            AuthorEntity(
                id = 13L,
                firebaseId = "firebaseIdTest2",
                name = "nameTest2",
                imageUrl = ""
            )
        )
    }
}