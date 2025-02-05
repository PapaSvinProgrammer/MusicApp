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
import org.mockito.kotlin.times

class GetAlbumTest {
    private val repository = mock<AlbumRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetAlbumAll(): Unit = runBlocking {
        val useCase = GetAlbum(repository)
        val testResult = listOf(Album(), Album())

        Mockito.`when`(repository.getAlbumAll()).thenReturn(testResult)

        val expected = listOf(Album(), Album())
        val actual = useCase.getAlbumAll()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAlbumAll()
    }

    @Test
    fun correctGetAlbumById(): Unit = runBlocking {
        val useCase = GetAlbum(repository)
        val testId = "testId"
        val testResult = Album(id = testId)

        Mockito.`when`(repository.getAlbumById(testId)).thenReturn(testResult)

        val expected = Album(id = testId)
        val actual = useCase.getAlbumById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getAlbumById(testId)
    }

    @Test
    fun invalidGetAlbumById(): Unit = runBlocking {
        val useCase = GetAlbum(repository)
        val testId = ""
        val testResult = Album(id = testId)

        Mockito.`when`(repository.getAlbumById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getAlbumById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getAlbumById(testId)
    }
}