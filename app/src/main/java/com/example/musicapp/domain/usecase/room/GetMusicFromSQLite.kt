package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class GetMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(): List<MusicResult?> {
        return musicLiteRepository.getAllMusic()
    }

    suspend fun execute(limit: Int): List<MusicResult?> {
       return musicLiteRepository.getMusicLimit(limit.toString())
    }
}