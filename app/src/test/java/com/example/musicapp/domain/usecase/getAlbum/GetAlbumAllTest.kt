package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetAlbumAllTest {
    val repository = mock<AlbumRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetAlbumsTest(): Unit = runBlocking {
        val useCase = GetAlbumAll(repository)
        val testResult = listOf(
            Album(),
            Album()
        )

        Mockito.`when`(repository.getAlbumAll()).thenReturn(testResult)

        val expected = listOf(
            Album(),
            Album()
        )
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}