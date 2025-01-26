package com.example.musicapp.domain.usecase.room.update

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class UpdatePlaylistImageTest {
    private val repository = mock<PlaylistRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctSaveImage(): Unit = runBlocking {
        val useCase = UpdatePlaylistImage(repository)
        val testUrl = "testUrl"
        val testId = 12L

        Mockito.`when`(
            repository.saveImage(
                url = testUrl,
                id = testId.toString()
            )
        ).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.saveImage(testUrl, testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).saveImage(testUrl, testId.toString())
    }

    @Test
    fun invalidSaveImage(): Unit = runBlocking {
        val useCase = UpdatePlaylistImage(repository)
        val testUrl = "testUrl"
        val testId = -1L

        Mockito.`when`(
            repository.saveImage(
                url = testUrl,
                id = testId.toString()
            )
        ).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.saveImage(testUrl, testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).saveImage(testUrl, testId.toString())
    }

    @Test
    fun correctDeleteImage(): Unit = runBlocking {
        val useCase = UpdatePlaylistImage(repository)
        val testId = 12L

        Mockito.`when`(repository.deleteImage(testId.toString())).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.deleteImage(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).deleteImage(testId.toString())
    }

    @Test
    fun invalidDeleteImage(): Unit = runBlocking {
        val useCase = UpdatePlaylistImage(repository)
        val testId = -1L

        Mockito.`when`(repository.deleteImage(testId.toString())).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.deleteImage(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).deleteImage(testId.toString())
    }
}