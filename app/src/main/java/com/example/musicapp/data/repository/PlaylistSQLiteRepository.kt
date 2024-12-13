package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class PlaylistSQLiteRepository(
    private val playlistDao: PlaylistDao
): PlaylistRepository {
    override suspend fun add(playlistEntity: PlaylistEntity) {
        playlistDao.insert(playlistEntity)
    }

    override suspend fun delete(id: String) {
        playlistDao.delete(id)
    }

    override suspend fun getAll(filter: String): List<PlaylistResult?> {
        val job = CoroutineScope(Dispatchers.IO).async {
            playlistDao.getAll(filter)
        }

        return job.await()
    }

    override suspend fun getOnlyPlaylist(filter: String): List<PlaylistEntity?> {
        val job = CoroutineScope(Dispatchers.IO).async {
            playlistDao.getAllOnlyPlaylist(filter)
        }

        return job.await()
    }
}