package com.example.musicapp.domain.usecase.room.get

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

class GetAuthorsFromSQLiteTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetAll(): Unit = runBlocking {
        val useCase = GetAuthorsFromSQLite(repository)
        val testResult = generateItems()

        Mockito.`when`(repository.getAllAuthor()).thenReturn(testResult)

        val expected = generateItems()
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAllAuthor()
    }

    @Test
    fun correctGetLimit(): Unit = runBlocking {
        val useCase = GetAuthorsFromSQLite(repository)
        val testResult = generateItems()
        val testLimit = 2

        Mockito.`when`(
            repository.getAuthorLimit(
                limit = testLimit
            )
        ).thenReturn(testResult)

        val expected = generateItems()
        val actual = useCase.execute(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAuthorLimit(testLimit)
    }

    @Test
    fun invalidGetLimit(): Unit = runBlocking {
        val useCase = GetAuthorsFromSQLite(repository)
        val testResult = generateItems()
        val testLimit = -7

        Mockito.`when`(
            repository.getAuthorLimit(
                limit = testLimit
            )
        ).thenReturn(testResult)

        val expected = listOf<AuthorEntity>()
        val actual = useCase.execute(testLimit)

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