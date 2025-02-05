package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    fun getPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getPlaylists()
    }

    fun getPlaylists(limit: Int): Flow<List<PlaylistEntity>> {
        if (limit <= 0) {
            return flow { listOf<PlaylistEntity>() }
        }

        return playlistRepository.getPlaylistsLimit(limit)
    }

    fun getPlaylistsOrderId(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getPlaylistsOrderId()
    }

    fun getPlaylistsOrderName(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getPlaylistsOrderName()
    }

    fun getPlaylistsOrderDate(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getPlaylistsOrderDate()
    }

    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        if (playlistId <= 0L) {
            return null
        }

        return playlistRepository.getPlaylistById(playlistId)
    }
}