package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryRoom(
    private val playlistDao: PlaylistDao
): PlaylistRepository {
    override suspend fun add(playlistEnum: PlaylistEntity) {
        playlistDao.insert(playlistEnum)
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

    override suspend fun saveImage(url: String, id: String) {
        playlistDao.saveImage(url, id)
    }

    override suspend fun saveName(name: String, id: String) {
        playlistDao.saveName(name, id)
    }

    override suspend fun getCount(): Int {
        return CoroutineScope(Dispatchers.IO).async {
            playlistDao.getCount()
        }.await()
    }

    override suspend fun getOnlyPlaylist(): List<PlaylistEntity> {
        return CoroutineScope(Dispatchers.IO).async {
            playlistDao.getOnlyPlaylist()
        }.await()
    }

    override suspend fun getOnlyPlaylistLimit(limit: Int): List<PlaylistEntity> {
        return CoroutineScope(Dispatchers.IO).async {
            playlistDao.getOnlyPlaylistLimit(limit)
        }.await()
    }

    override fun getAllById(): Flow<List<PlaylistResult>> {
        return playlistDao.getAllById()
    }

    override fun getAllByName(): Flow<List<PlaylistResult>> {
        return playlistDao.getAllByName()
    }

    override fun getAllByDate(): Flow<List<PlaylistResult>> {
        return playlistDao.getAllByDate()
    }

    override suspend fun search(text: String): List<PlaylistResult> {
        return CoroutineScope(Dispatchers.IO).async {
            playlistDao.searchPlaylist("%$text%")
        }.await()
    }
}