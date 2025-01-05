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

class GetAlbumByIdTest {
    private val repository = mock<AlbumRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetAlbum(): Unit = runBlocking {
        val useCase = GetAlbumById(repository)
        val testId = "testId"
        val testResult = Album(
            id = testId
        )

        Mockito.`when`(repository.getAlbumById(testId)).thenReturn(testResult)

        val actual = useCase.execute(testId)

        Assertions.assertEquals(testResult, actual)
    }

    @Test
    fun invalidGetAlbumNeverStart(): Unit = runBlocking {
        val useCase = GetAlbumById(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getAlbumById(testId)).thenReturn(testResult)
        useCase.execute(testId)

        Mockito.verify(repository, never()).getAlbumById(any())
    }

    @Test
    fun invalidGetAlbum(): Unit = runBlocking {
        val useCase = GetAlbumById(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getAlbumById(testId)).thenReturn(testResult)

        val actual = useCase.execute(testId)

        Assertions.assertEquals(testResult, actual)
    }
}