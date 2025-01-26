package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.domain.repository.DownloadMusicRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class DeleteDownloadMusicTest {
    private val repository = mock<DownloadMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDelete() {
        val useCase = DeleteDownloadMusic(repository)
        val testId = "musicId"

        Mockito.doNothing().`when`(repository).remove(testId)

        val expected = Unit
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).remove(testId)
    }

    @Test
    fun invalidDelete() {
        val useCase = DeleteDownloadMusic(repository)
        val testId = ""

        Mockito.doNothing().`when`(repository).remove(testId)

        val expected = Unit
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).remove(testId)
    }
}