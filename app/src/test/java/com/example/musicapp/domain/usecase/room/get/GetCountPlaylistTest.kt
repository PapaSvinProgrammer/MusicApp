package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class GetCountPlaylistTest {
    private val repository = mock<PlaylistRepository>()

    @Test
    fun `correct get count playlists`(): Unit = runBlocking {
        val useCase = GetCountPlaylist(repository)
        val testResult = flowOf(3)

        Mockito.`when`(repository.getCountPlaylist()).thenReturn(testResult)

        val expected = 3
        var actual = -3

        useCase.execute().collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getCountPlaylist()
    }
}