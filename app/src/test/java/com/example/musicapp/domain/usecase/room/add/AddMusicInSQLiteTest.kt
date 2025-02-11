package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.app.support.ConvertMusic
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository
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
    fun `correct add music in SQLite`(): Unit = runBlocking {
        val useCase = AddMusicInSQLite(repository)
        val testMusic = Music()
        val testPlaylistId = 2L

        Mockito.`when`(
            repository.add(
                musicResult = ConvertMusic.convertToMusicResult(testMusic),
                playlistId = testPlaylistId
            )
        ).thenReturn(Unit)

        val expected = Unit
        val actual = useCase.execute(testMusic, testPlaylistId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).add(ConvertMusic.convertToMusicResult(testMusic), testPlaylistId)
    }

    @Test
    fun `invalid music object with add music in SQLite`(): Unit = runBlocking {
        val useCase = AddMusicInSQLite(repository)
        val testMusic = null
        val testPlaylistId = 2L

        val expected = Unit
        val actual = useCase.execute(testMusic, testPlaylistId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).add(any(), any())
    }

    @Test
    fun `invalid playlist id with add music in SQLite`(): Unit = runBlocking {
        val useCase = AddMusicInSQLite(repository)
        val testMusic = Music()
        val testPlaylistId = -2L

        val expected = Unit
        val actual = useCase.execute(testMusic, testPlaylistId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).add(any(), any())
    }
}