package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class GetMusicsByAlbumIdTest {
    private val repository = mock<MusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetMusicsByAlbumId(repository)
        val testId = "albumId"
        val testResult = listOf(Music(albumId = testId), Music(albumId = testId))

        Mockito.`when`(repository.getMusicsByAlbumId(testId)).thenReturn(testResult)

        val expected = listOf(Music(albumId = testId), Music(albumId = testId))
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultReturn(): Unit = runBlocking {
        val useCase = GetMusicsByAlbumId(repository)
        val testId = ""
        val testResult = listOf<Music>()

        Mockito.`when`(repository.getMusicsByAlbumId(testId)).thenReturn(testResult)

        val expected = listOf<Music>()
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultNeverStart(): Unit = runBlocking {
        val useCase = GetMusicsByAlbumId(repository)
        val testId = ""
        val testResult = listOf<Music>()

        Mockito.`when`(repository.getMusicsByAlbumId(testId)).thenReturn(testResult)
        useCase.execute(testId)

        Mockito.verify(repository, never()).getMusicsByAlbumId(testId)
    }
}