package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class DeletePlaylistFromSQLiteTest {
    private val repository = mock<PlaylistRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDataInput(): Unit = runBlocking {
        val useCase = DeletePlaylistFromSQLite(repository)
        val testData = 14L

        useCase.execute(testData)

        Mockito.verify(repository, times(1)).delete(testData.toString())
    }

    @Test
    fun invalidDataInput(): Unit = runBlocking {
        val useCase = DeletePlaylistFromSQLite(repository)
        val testData = -1L

        useCase.execute(testData)

        Mockito.verify(repository, never()).delete(testData.toString())
    }
}