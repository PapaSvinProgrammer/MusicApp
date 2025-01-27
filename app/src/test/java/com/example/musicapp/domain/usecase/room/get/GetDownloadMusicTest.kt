package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetDownloadMusicTest {
    private val repository = mock<DownloadMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetDownloads() {
        val useCase = GetDownloadedMusic(repository)
        val testResult = listOf(Music(), Music())

        Mockito.`when`(repository.getDownloads()).thenReturn(testResult)

        val expected = listOf(Music(), Music())
        val actual = useCase.getDownloads()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getDownloads()
    }

    @Test
    fun correctGetDownloadsLimit() {
        val useCase = GetDownloadedMusic(repository)
        val testResult = listOf(Music(), Music(), Music())
        val testLimit = 3

        Mockito.`when`(repository.getDownloadsLimit(testLimit)).thenReturn(testResult)

        val expected = listOf(Music(), Music(), Music())
        val actual = useCase.getDownloadsLimit(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getDownloadsLimit(testLimit)
    }

    @Test
    fun invalidGetDownloadsLimit() {
        val useCase = GetDownloadedMusic(repository)
        val testResult = listOf(Music(), Music(), Music())
        val testLimit = -1

        Mockito.`when`(repository.getDownloadsLimit(testLimit)).thenReturn(testResult)

        val expected = listOf<Music>()
        val actual = useCase.getDownloadsLimit(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getDownloadsLimit(testLimit)
    }

    @Test
    fun correctGetDownload() {
        val useCase = GetDownloadedMusic(repository)
        val tesId = "musicId"
        val testResult = Music()

        Mockito.`when`(repository.getDownload(tesId)).thenReturn(testResult)

        val expected = Music()
        val actual = useCase.getDownload(tesId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getDownload(tesId)
    }

    @Test
    fun invalidGetDownload() {
        val useCase = GetDownloadedMusic(repository)
        val tesId = ""
        val testResult = Music()

        Mockito.`when`(repository.getDownload(tesId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getDownload(tesId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getDownload(tesId)
    }
}