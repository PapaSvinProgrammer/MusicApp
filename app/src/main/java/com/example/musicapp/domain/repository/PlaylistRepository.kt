package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun add(playlistEnum: PlaylistEntity)
    suspend fun saveImage(url: String, id: String)
    suspend fun saveName(name: String, id: String)
    suspend fun search(text: String): List<PlaylistEntity>

    suspend fun delete(id: String)
    suspend fun deleteImage(id: String)

    fun getCountPlaylist(): Flow<Int>
    fun getCountMusicInPlaylist(playlistId: Long): Flow<Int>
    fun getTimePlaylist(playlistId: Long): Flow<Int>

    fun getPlaylistsLimit(limit: Int): Flow<List<PlaylistEntity>>
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    fun getPlaylistsOrderId(): Flow<List<PlaylistEntity>>
    fun getPlaylistsOrderName(): Flow<List<PlaylistEntity>>
    fun getPlaylistsOrderDate(): Flow<List<PlaylistEntity>>

    fun getMusicsPlaylist(playlistId: Long): Flow<List<MusicResult>>
    fun getMusicsPlaylist(playlistId: Long, limit: Int): Flow<List<MusicResult>>
}