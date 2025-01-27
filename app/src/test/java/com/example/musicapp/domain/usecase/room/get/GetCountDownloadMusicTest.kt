package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.DownloadMusicRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class GetCountDownloadMusicTest {
    private val repository = mock<DownloadMusicRepository>()

    @Test
    fun correctGetCount() {
        val useCase = GetCountDownloadMusic(repository)
        val testResult = 3

        Mockito.`when`(repository.getCount()).thenReturn(testResult)

        val expected = 3
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getCount()
    }
}