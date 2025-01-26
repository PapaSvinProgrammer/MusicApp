package com.example.musicapp.domain.usecase.room.update

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class UpdatePlaylistNameTest {
    private val repository = mock<PlaylistRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctUpdate(): Unit = runBlocking {
        val useCase = UpdatePlaylistName(repository)
        val testId = 12L
        val testName = "testName"

        Mockito.`when`(
            repository.saveName(
                name = testName,
                id = testId.toString()
            )
        ).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testName, testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).saveName(testName, testId.toString())
    }

    @Test
    fun invalidUpdate(): Unit = runBlocking {
        val useCase = UpdatePlaylistName(repository)
        val testId = -1L
        val testName = "testName"

        Mockito.`when`(
            repository.saveName(
                name = testName,
                id = testId.toString()
            )
        ).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testName, testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).saveName(testName, testId.toString())
    }
}