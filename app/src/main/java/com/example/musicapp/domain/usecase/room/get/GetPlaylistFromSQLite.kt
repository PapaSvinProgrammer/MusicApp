package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    suspend fun getOnlyPlaylists(): List<PlaylistEntity> {
        return playlistRepository.getOnlyPlaylist()
    }

    suspend fun getOnlyPlaylists(limit: Int): List<PlaylistEntity> {
        if (limit <= 0) {
            return listOf()
        }

        return playlistRepository.getOnlyPlaylistLimit(limit)
    }

    suspend fun getById(id: Long): PlaylistResult? {
        if (id <= 0L) {
            return null
        }

        return playlistRepository.getById(id)
    }

    fun getAllOrderId(): Flow<List<PlaylistResult>> {
        return playlistRepository.getAllOrderId()
    }

    fun getAllOrderName(): Flow<List<PlaylistResult>> {
        return playlistRepository.getAllOrderName()
    }

    fun getAllOrderDate(): Flow<List<PlaylistResult>> {
        return playlistRepository.getAllOrderDate()
    }
}