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

class GetMusicsByAuthorIdTest {
    private val repository = mock<MusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetMusicsByAuthorId(repository)
        val testId = "authorId"
        val testResult = listOf(Music(groupId = testId), Music(groupId = testId))

        Mockito.`when`(repository.getMusicByAuthorIdOrderRating(testId)).thenReturn(testResult)

        val expected = listOf(Music(groupId = testId), Music(groupId = testId))
        val actual = useCase.executeOrderRating(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultReturn(): Unit = runBlocking {
        val useCase = GetMusicsByAuthorId(repository)
        val testId = ""
        val testResult = listOf<Music>()

        Mockito.`when`(repository.getMusicByAuthorIdOrderRating(testId)).thenReturn(testResult)

        val expected = listOf<Music>()
        val actual = useCase.executeOrderRating(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultNeverStart(): Unit = runBlocking {
        val useCase = GetMusicsByAuthorId(repository)
        val testId = ""
        val testResult = listOf<Music>()

        Mockito.`when`(repository.getMusicByAuthorIdOrderRating(testId)).thenReturn(testResult)
        useCase.executeOrderRating(testId)

        Mockito.verify(repository, never()).getMusicByAuthorIdOrderRating(testId)
    }
}