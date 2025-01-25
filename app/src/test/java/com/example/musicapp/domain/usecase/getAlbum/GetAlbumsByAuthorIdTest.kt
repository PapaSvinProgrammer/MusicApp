package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class GetAlbumsByAuthorIdTest {
    private val repository = mock<AlbumRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctOrderRating(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testId = "authorId"
        val testResult = listOf(Album(id = testId), Album(id = testId))

        Mockito.`when`(repository.getAlbumByAuthorIdOrderRating(testId)).thenReturn(testResult)

        val expected = listOf(Album(id = testId), Album(id = testId))
        val actual = useCase.executeOrderRating(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctOrderDate(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testId = "authorId"
        val testResult = listOf(Album(id = testId), Album(id = testId))

        Mockito.`when`(repository.getAlbumByAuthorIdOrderDate(testId)).thenReturn(testResult)

        val expected = listOf(Album(id = testId), Album(id = testId))
        val actual = useCase.executeOrderDate(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctOrderName(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testId = "authorId"
        val testResult = listOf(Album(id = testId), Album(id = testId))

        Mockito.`when`(repository.getAlbumByAuthorIdOrderName(testId)).thenReturn(testResult)

        val expected = listOf(Album(id = testId), Album(id = testId))
        val actual = useCase.executeOrderName(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidOrderRating(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testId = ""
        val testResult = listOf<Album>()

        Mockito.`when`(repository.getAlbumByAuthorIdOrderRating(testId)).thenReturn(testResult)

        val expected = listOf<Album>()
        val actual = useCase.executeOrderRating(testId)

        Mockito.verify(repository, never()).getAlbumByAuthorIdOrderRating(testId)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidOrderDate(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testId = ""
        val testResult = listOf<Album>()

        Mockito.`when`(repository.getAlbumByAuthorIdOrderDate(testId)).thenReturn(testResult)

        val expected = listOf<Album>()
        val actual = useCase.executeOrderDate(testId)

        Mockito.verify(repository, never()).getAlbumByAuthorIdOrderDate(testId)
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidOrderName(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testId = ""
        val testResult = listOf<Album>()

        Mockito.`when`(repository.getAlbumByAuthorIdOrderName(testId)).thenReturn(testResult)

        val expected = listOf<Album>()
        val actual = useCase.executeOrderName(testId)

        Mockito.verify(repository, never()).getAlbumByAuthorIdOrderName(testId)
        Assertions.assertEquals(expected, actual)
    }
}