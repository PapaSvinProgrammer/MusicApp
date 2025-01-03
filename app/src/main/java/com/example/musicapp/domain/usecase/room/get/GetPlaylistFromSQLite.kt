package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    fun getOnlyPlaylists(): Flow<List<PlaylistEntity?>> {
        return playlistRepository.getOnlyPlaylist()
    }

    suspend fun getById(id: Long): PlaylistResult? {
        return playlistRepository.getById(id.toString())
    }

    fun getAllById(): Flow<List<PlaylistResult?>> {
        return playlistRepository.getAllById()
    }

    fun getAllByName(): Flow<List<PlaylistResult?>> {
        return playlistRepository.getAllByName()
    }

    fun getAllByDate(): Flow<List<PlaylistResult?>> {
        return playlistRepository.getAllByDate()
    }
}