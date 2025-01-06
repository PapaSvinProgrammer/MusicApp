package com.example.musicapp.domain.usecase.getAnother

import com.example.musicapp.domain.module.MusicText
import com.example.musicapp.domain.repository.MusicTextRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class GetMusicTextTest {
    private val repository = mock<MusicTextRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetMusicText(repository)
        val testId = "testId"
        val testResult = MusicText(
            id = testId
        )

        Mockito.`when`(repository.getTextById(testId)).thenReturn(testResult)

        val expected = MusicText(
            id = testId
        )
        val actual = useCase.getTextById(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultNeverStart(): Unit = runBlocking {
        val useCase = GetMusicText(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getTextById(testId)).thenReturn(testResult)
        useCase.getTextById(testId)

        Mockito.verify(repository, never()).getTextById(any())
    }

    @Test
    fun invalidResultReturn(): Unit = runBlocking {
        val useCase = GetMusicText(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getTextById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getTextById(testId)

        Assertions.assertEquals(expected, actual)
    }
}