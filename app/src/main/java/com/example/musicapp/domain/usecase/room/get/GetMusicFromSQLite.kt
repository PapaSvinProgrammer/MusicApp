package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun getAllMusic(): List<MusicResult> {
        return musicLiteRepository.getAllMusic()
    }

    suspend fun getAllMusic(limit: Int): List<MusicResult> {
        if (limit <= 0) {
            return listOf()
        }
        return musicLiteRepository.getMusicLimit(limit)
    }

    fun getAllMusicFromPlaylist(playlistId: Long): Flow<List<MusicResult>> {
        if (playlistId <= 0L) {
            return flowOf()
        }

        return musicLiteRepository.getAllMusicFromPlaylist(playlistId)
    }

    suspend fun getAllMusicFromPlaylist(playlistId: Long, limit: Int): List<MusicResult> {
        if (playlistId <= 0L || limit <= 0) {
            return listOf()
        }

        return musicLiteRepository.getAllMusicFromPlaylist(
            playlistId = playlistId,
            limit = limit
        )
    }
}