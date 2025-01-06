package com.example.musicapp.domain.usecase.getAnother

import com.example.musicapp.domain.module.MusicInfo
import com.example.musicapp.domain.repository.MusicInfoRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class GetMusicInfoTest {
    private val repository = mock<MusicInfoRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetMusicInfo(repository)
        val testId = "testId"
        val testResult = MusicInfo(
            id = testId
        )

        Mockito.`when`(repository.getInfoById(testId)).thenReturn(testResult)

        val expected = MusicInfo(
            id = testId
        )
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultNeverStart(): Unit = runBlocking {
        val useCase = GetMusicInfo(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getInfoById(testId)).thenReturn(testResult)
        useCase.execute(testId)

        Mockito.verify(repository, never()).getInfoById(any())
    }

    @Test
    fun invalidResultReturn(): Unit = runBlocking {
        val useCase = GetMusicInfo(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getInfoById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }
}