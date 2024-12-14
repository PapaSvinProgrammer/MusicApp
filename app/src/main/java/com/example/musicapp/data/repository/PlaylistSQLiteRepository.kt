package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistSQLiteRepository(
    private val playlistDao: PlaylistDao
): PlaylistRepository {
    override suspend fun add(playlistEntity: PlaylistEntity) {
        playlistDao.insert(playlistEntity)
    }

    override suspend fun delete(id: String) {
        playlistDao.delete(id)
    }

    override fun getAll(filter: String): Flow<List<PlaylistResult?>> {
        return playlistDao.getAll(filter)
    }

    override fun getOnlyPlaylist(filter: String): Flow<List<PlaylistEntity?>> {
        return playlistDao.getAllOnlyPlaylist(filter)
    }
}