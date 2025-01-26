package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository
import com.example.musicapp.support.convertAnother.ConvertMusic
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class AddMusicInSQLiteTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctAdd(): Unit = runBlocking {
        val useCase = AddMusicInSQLite(repository)

        val testMusic = Music()
        val testMusicResult = ConvertMusic().convertToMusicResult(
            testMusic,
            1
        )

        Mockito.`when`(repository.add(testMusicResult)).thenReturn(Unit)

        useCase.execute(testMusic)

        Mockito.verify(repository, times(1)).add(testMusicResult)
    }

    @Test
    fun invalidAdd(): Unit = runBlocking {
        val useCase = AddMusicInSQLite(repository)

        val testMusic = null

        Mockito.`when`(repository.add(any())).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testMusic)

        Mockito.verify(repository, never()).add(any())
        Assertions.assertEquals(expected, actual)
    }
}