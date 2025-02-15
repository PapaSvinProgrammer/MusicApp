package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    fun getAllMusic(): Flow<List<MusicResult>> {
        return musicLiteRepository.getAllMusic()
    }

    fun getAllMusic(limit: Int): Flow<List<MusicResult>> {
        if (limit <= 0) {
            return flow { emit(listOf()) }
        }
        return musicLiteRepository.getMusicLimit(limit)
    }
}