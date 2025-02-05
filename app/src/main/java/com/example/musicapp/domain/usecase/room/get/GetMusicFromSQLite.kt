package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    fun getAllMusic(): Flow<List<MusicResult>> {
        return musicLiteRepository.getAllMusic()
    }

    fun getAllMusic(limit: Int): Flow<List<MusicResult>> {
        if (limit <= 0) {
            return flow { listOf<MusicResult>() }
        }
        return musicLiteRepository.getMusicLimit(limit)
    }
}