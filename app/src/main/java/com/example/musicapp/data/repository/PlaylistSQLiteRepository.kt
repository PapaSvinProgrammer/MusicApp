package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistSQLiteRepository(
    private val playlistDao: PlaylistDao
): PlaylistRepository {
    override suspend fun add(playlistEntity: PlaylistEntity) {
        playlistDao.insert(playlistEntity)
    }

    override suspend fun delete(id: String) {
        playlistDao.delete(id)
    }

    override suspend fun getById(id: String): PlaylistResult? {
        val job = CoroutineScope(Dispatchers.IO).async {
            playlistDao.getById(id)
        }

        return job.await()
    }

    override fun getAllById(): Flow<List<PlaylistResult?>> {
        return playlistDao.getAllById()
    }

    override fun getAllByName(): Flow<List<PlaylistResult?>> {
        return playlistDao.getAllByName()
    }

    override fun getAllByDate(): Flow<List<PlaylistResult?>> {
        return playlistDao.getAllByDate()
    }

    override fun getOnlyPlaylist(): Flow<List<PlaylistEntity?>> {
        return playlistDao.getOnlyPlaylist()
    }
}