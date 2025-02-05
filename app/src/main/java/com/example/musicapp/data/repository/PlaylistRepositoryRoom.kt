package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
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
    
    override suspend fun saveImage(url: String, id: String) {
        playlistDao.saveImage(url, id)
    }

    override suspend fun deleteImage(id: String) {
        playlistDao.saveImage("", id)
    }

    override suspend fun saveName(name: String, id: String) {
        playlistDao.saveName(name, id)
    }

    override suspend fun search(text: String): List<PlaylistEntity> {
        return CoroutineScope(Dispatchers.IO).async {
            playlistDao.search("%$text%")
        }.await()
    }

    override fun getCountPlaylist(): Flow<Int> {
        return playlistDao.getCount()
    }

    override fun getCountMusicInPlaylist(playlistId: Long): Flow<Int> {
        return playlistDao.getCountMusicInPlaylist(playlistId)
    }

    override fun getTimePlaylist(playlistId: Long): Flow<Int> {
        return playlistDao.getTime(playlistId)
    }

    override fun getPlaylistsLimit(limit: Int): Flow<List<PlaylistEntity>> {
        return playlistDao.getOnlyPlaylistLimit(limit)
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistEntity? {
        return CoroutineScope(Dispatchers.IO).async {
            playlistDao.getPlaylistById(playlistId)
        }.await()
    }

    override fun getPlaylistsOrderId(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllById()
    }

    override fun getPlaylistsOrderName(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllByName()
    }

    override fun getPlaylistsOrderDate(): Flow<List<PlaylistEntity>> {
        return playlistDao.getAllByDate()
    }

    override fun getMusicsPlaylist(playlistId: Long): Flow<List<MusicResult>> {
        return playlistDao.getMusicsPlaylist(playlistId)
    }

    override fun getMusicsPlaylist(playlistId: Long, limit: Int): Flow<List<MusicResult>> {
        return playlistDao.getMusicsPlaylist(playlistId, limit)
    }
}