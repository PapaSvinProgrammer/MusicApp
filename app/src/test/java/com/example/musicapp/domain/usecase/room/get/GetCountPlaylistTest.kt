package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class GetCountPlaylistTest {
    private val repository = mock<PlaylistRepository>()

    @Test
    fun correctExecute(): Unit = runBlocking {
        val useCase = GetCountPlaylist(repository)
        val testResult = 3

        Mockito.`when`(repository.getCount()).thenReturn(testResult)

        val expected = 3
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getCount()
    }
}