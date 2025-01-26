package com.example.musicapp.domain.usecase.room.downloadMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class DownloadMusicTest {
    private val repository = mock<DownloadMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDownload() {
        val useCase = DownloadMusic(repository)
        val testId = "musicId"
        val testMusic = Music(id = testId)

        Mockito.doNothing().`when`(repository).download(testMusic)

        val expected = Unit
        val actual = useCase.execute(testMusic)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).download(testMusic)
    }

    @Test
    fun invalidDownload() {
        val useCase = DownloadMusic(repository)
        val testId = null
        val testMusic = Music(id = testId)

        Mockito.doNothing().`when`(repository).download(testMusic)

        val expected = Unit
        val actual = useCase.execute(testMusic)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).download(testMusic)
    }
}