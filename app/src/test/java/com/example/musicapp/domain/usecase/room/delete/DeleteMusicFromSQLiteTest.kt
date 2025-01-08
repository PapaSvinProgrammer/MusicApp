package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class DeleteMusicFromSQLiteTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDataInput(): Unit = runBlocking {
        val useCase = DeleteMusicFromSQLite(repository)
        val testData = "testId"

        useCase.execute(testData)

        Mockito.verify(repository, times(1)).delete(testData)
    }

    @Test
    fun invalidDataInput(): Unit = runBlocking {
        val useCase = DeleteMusicFromSQLite(repository)
        val testData = ""

        useCase.execute(testData)

        Mockito.verify(repository, never()).delete(testData)
    }
}