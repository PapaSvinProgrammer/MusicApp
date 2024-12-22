package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun add(playlistEnum: PlaylistEntity)
    suspend fun delete(id: String)
    suspend fun getById(id: String): PlaylistResult?
    suspend fun saveImage(url: String, id: String)
    suspend fun saveName(name: String, id: String)
    fun getAllById(): Flow<List<PlaylistResult?>>
    fun getAllByName(): Flow<List<PlaylistResult?>>
    fun getAllByDate(): Flow<List<PlaylistResult?>>
    fun getOnlyPlaylist(): Flow<List<PlaylistEntity?>>
}