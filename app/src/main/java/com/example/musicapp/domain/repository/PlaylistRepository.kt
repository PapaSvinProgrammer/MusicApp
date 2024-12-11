package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult

interface PlaylistRepository {
    suspend fun add(playlistEnum: PlaylistEntity)
    suspend fun delete(id: String)
    suspend fun getAll(): List<PlaylistResult?>
    suspend fun getOnlyPlaylist(): List<PlaylistEntity?>
}