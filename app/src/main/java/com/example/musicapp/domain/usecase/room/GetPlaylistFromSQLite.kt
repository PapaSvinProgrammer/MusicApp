package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository

class GetPlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    suspend fun executeToAll(): List<PlaylistResult?> {
        return playlistRepository.getAll()
    }

    suspend fun executeToLimit(): List<PlaylistEntity?> {
        return playlistRepository.getOnlyPlaylist()
    }
}