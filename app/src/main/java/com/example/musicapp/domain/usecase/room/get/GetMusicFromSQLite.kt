package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class GetMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(): List<MusicResult> {
        return musicLiteRepository.getAllMusic()
    }

    suspend fun execute(limit: Int): List<MusicResult> {
        if (limit <= 0) {
            return listOf()
        }

        return musicLiteRepository.getMusicLimit(limit)
    }
}