package com.example.musicapp.domain.usecase.room.find

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository
import com.example.musicapp.support.convertAnother.ConvertMusic
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class FindFavoriteMusicFromSQLiteTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctFind(): Unit = runBlocking {
        val useCase = FindFavoriteMusicFromSQLite(repository)
        val testId = "testId"
        val testResult = ConvertMusic().convertToMusicResult(Music(id=testId),1)

        Mockito.`when`(repository.findUserById(testId)).thenReturn(testResult)

        val expected = ConvertMusic().convertToMusicResult(Music(id=testId),1)
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).findUserById(testId)
    }

    @Test
    fun invalidFind(): Unit = runBlocking {
        val useCase = FindFavoriteMusicFromSQLite(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.findUserById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).findUserById(testId)
    }
}