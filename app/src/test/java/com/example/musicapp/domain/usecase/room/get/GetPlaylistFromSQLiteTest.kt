package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetPlaylistFromSQLiteTest {
    private val repository = mock<PlaylistRepository>()
    private val playlistEntity = mock<PlaylistEntity>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
        Mockito.reset(playlistEntity)
    }

    @Test
    fun `correct get playlists with limit`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testLimit = 2
        val testResult = flowOf(listOf(playlistEntity, playlistEntity))

        Mockito.`when`(repository.getPlaylistsLimit(testLimit)).thenReturn(testResult)

        val expected = listOf(playlistEntity, playlistEntity)
        var actual = listOf<PlaylistEntity>()

        useCase.getPlaylists(testLimit).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylistsLimit(testLimit)
    }

    @Test
    fun `invalid get playlists with limit`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testLimit = -2
        val testResult = flowOf(listOf(playlistEntity, playlistEntity))

        Mockito.`when`(repository.getPlaylistsLimit(testLimit)).thenReturn(testResult)

        val expected = listOf<PlaylistEntity>()
        var actual: List<PlaylistEntity>? = null

        useCase.getPlaylists(testLimit).collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getPlaylistsLimit(testLimit)
    }

    @Test
    fun `correct get playlists order by id`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = flowOf(listOf(playlistEntity, playlistEntity))

        Mockito.`when`(repository.getPlaylistsOrderId()).thenReturn(testResult)

        val expected = listOf(playlistEntity, playlistEntity)
        var actual = listOf<PlaylistEntity>()

        useCase.getPlaylistsOrderId().collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylistsOrderId()
    }

    @Test
    fun `correct get playlists order by name`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = flowOf(listOf(playlistEntity, playlistEntity))

        Mockito.`when`(repository.getPlaylistsOrderName()).thenReturn(testResult)

        val expected = listOf(playlistEntity, playlistEntity)
        var actual = listOf<PlaylistEntity>()

        useCase.getPlaylistsOrderName().collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylistsOrderName()
    }

    @Test
    fun `correct get playlists order by date`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = flowOf(listOf(playlistEntity, playlistEntity))

        Mockito.`when`(repository.getPlaylistsOrderDate()).thenReturn(testResult)

        val expected = listOf(playlistEntity, playlistEntity)
        var actual = listOf<PlaylistEntity>()

        useCase.getPlaylistsOrderDate().collect {
            actual = it
        }

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylistsOrderDate()
    }

    @Test
    fun `correct get playlist by id`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testId = 4L
        val testResult = playlistEntity

        Mockito.`when`(repository.getPlaylistById(testId)).thenReturn(testResult)

        val expected = playlistEntity
        val actual = useCase.getPlaylistById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylistById(testId)
    }

    @Test
    fun `invalid get playlist by id`(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testId = -4L
        val testResult = playlistEntity

        Mockito.`when`(repository.getPlaylistById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getPlaylistById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getPlaylistById(testId)
    }
}