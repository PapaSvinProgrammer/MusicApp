package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.domain.repository.SaveMusicRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class DeleteSaveMusicFromSQLiteTest {
    private val repository = mock<SaveMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDelete(): Unit = runBlocking {
        val useCase = DeleteSaveMusicFromSQLite(repository)
        val testId = "musicId"

        Mockito.`when`(repository.delete(testId)).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).delete(testId)
    }

    @Test
    fun invalidDelete(): Unit = runBlocking {
        val useCase = DeleteSaveMusicFromSQLite(repository)
        val testId = ""

        Mockito.`when`(repository.delete(testId)).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).delete(testId)
    }
}