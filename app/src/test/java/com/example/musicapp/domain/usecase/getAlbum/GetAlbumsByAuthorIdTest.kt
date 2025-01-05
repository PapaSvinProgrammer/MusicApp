package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class GetAlbumsByAuthorIdTest {
    private val repository = mock<AlbumRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetAlbums(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testAuthorId = "authorId"
        val testResult = listOf(
            Album(
                groupId = testAuthorId
            ),
            Album(
                groupId = testAuthorId
            )
        )

        Mockito.`when`(repository.getAlbumByAuthorId(testAuthorId)).thenReturn(testResult)

        val expected = listOf(
            Album(
                groupId = testAuthorId
            ),
            Album(
                groupId = testAuthorId
            )
        )
        val actual = useCase.execute(testAuthorId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidGetAlbumsNeverStart(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testAuthorId = ""
        val testResult = listOf<Album>()

        Mockito.`when`(repository.getAlbumByAuthorId(testAuthorId)).thenReturn(testResult)
        useCase.execute(testAuthorId)

        Mockito.verify(repository, never()).getAlbumByAuthorId(any())
    }

    @Test
    fun invalidGetAlbums(): Unit = runBlocking {
        val useCase = GetAlbumsByAuthorId(repository)
        val testAuthorId = ""
        val testResult = listOf<Album>()

        Mockito.`when`(repository.getAlbumByAuthorId(testAuthorId)).thenReturn(testResult)

        val expected = listOf<Album>()
        val actual = useCase.execute(testAuthorId)

        Assertions.assertEquals(expected, actual)
    }
}