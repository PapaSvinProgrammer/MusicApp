package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.saveMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class AddSaveMusicInSQLiteTest {
    private val repository = mock<SaveMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctAdd(): Unit = runBlocking {
        val useCase = AddSaveMusicInSQLite(repository)
        val testId = "musicId"
        val testItem = SaveMusicEntity(
            id = 0,
            musicId = testId
        )

        Mockito.`when`(repository.add(testItem)).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).add(testItem)
    }

    @Test
    fun invalidAdd(): Unit = runBlocking {
        val useCase = AddSaveMusicInSQLite(repository)
        val testId = ""

        Mockito.`when`(repository.add(any())).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).add(any())
    }
}