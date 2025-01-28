package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

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

    suspend fun getAllMusicFromPlaylist(playlistId: Long): List<MusicResult> {
        if (playlistId <= 0L) {
            return listOf()
        }

        return musicLiteRepository.getAllMusicFromPlaylist(playlistId)
    }
}