package com.example.musicapp.domain.usecase.room.downloadMusic

import com.example.musicapp.domain.repository.DownloadMusicRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class ManageDownloadTest {
    private val repository = mock<DownloadMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun stopTest() {
        val useCase = ManageDownload(repository)

        Mockito.doNothing().`when`(repository).stop()

        val expected = Unit
        val actual = useCase.stop()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).stop()
    }

    @Test
    fun resumeTest() {
        val useCase = ManageDownload(repository)

        Mockito.doNothing().`when`(repository).resume()

        val expected = Unit
        val actual = useCase.resume()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).resume()
    }
}