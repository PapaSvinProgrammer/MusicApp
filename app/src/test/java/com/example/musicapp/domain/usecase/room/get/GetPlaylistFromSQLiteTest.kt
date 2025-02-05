package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
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
    private val playlistResult = mock<PlaylistResult>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
        Mockito.reset(playlistEntity)
        Mockito.reset(playlistResult)
    }

    @Test
    fun correctGetPlaylists(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = listOf(playlistEntity, playlistEntity)

        Mockito.`when`(repository.getPlaylists()).thenReturn(testResult)

        val expected = listOf(playlistEntity, playlistEntity)
        val actual = useCase.getPlaylists()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylists()
    }

    @Test
    fun correctGetPlaylistsLimit(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = listOf(playlistEntity, playlistEntity)
        val testLimit = 2

        Mockito.`when`(
            repository.getPlaylistsLimit(testLimit)
        ).thenReturn(testResult)

        val expected = listOf(playlistEntity, playlistEntity)
        val actual = useCase.getPlaylists(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getPlaylistsLimit(testLimit)
    }

    @Test
    fun invalidGetPlaylistsLimit(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = listOf(playlistEntity, playlistEntity)
        val testLimit = -2

        Mockito.`when`(
            repository.getPlaylistsLimit(testLimit)
        ).thenReturn(testResult)

        val expected = listOf<PlaylistEntity>()
        val actual = useCase.getPlaylists(testLimit)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getPlaylistsLimit(testLimit)
    }

    @Test
    fun correctGetById(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testId = 2L
        val testResult = playlistResult

        Mockito.`when`(repository.getById(testId)).thenReturn(testResult)

        val expected = playlistResult
        val actual = useCase.getById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getById(testId)
    }

    @Test
    fun invalidGetById(): Unit = runBlocking {
        val useCase = GetPlaylistFromSQLite(repository)
        val testId = -1L
        val testResult = playlistResult

        Mockito.`when`(repository.getById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getById(testId)
    }

    @Test
    fun correctGetAllOrderId() {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = flowOf<List<PlaylistResult>>()

        Mockito.`when`(repository.getAllOrderId()).thenReturn(testResult)

        val actual = useCase.getAllOrderId()

        Assertions.assertEquals(testResult, actual)
        Mockito.verify(repository, times(1)).getAllOrderId()
    }

    @Test
    fun correctGetPlaylistsOrderName() {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = flowOf<List<PlaylistResult>>()

        Mockito.`when`(repository.getAllOrderName()).thenReturn(testResult)

        val actual = useCase.getPlaylistsOrderName()

        Assertions.assertEquals(testResult, actual)
        Mockito.verify(repository, times(1)).getAllOrderName()
    }

    @Test
    fun correctGetPlaylistsOrderDate() {
        val useCase = GetPlaylistFromSQLite(repository)
        val testResult = flowOf<List<PlaylistResult>>()

        Mockito.`when`(repository.getAllOrderDate()).thenReturn(testResult)

        val actual = useCase.getPlaylistsOrderDate()

        Assertions.assertEquals(testResult, actual)
        Mockito.verify(repository, times(1)).getAllOrderDate()
    }
}