package com.example.musicapp.domain.usecase.room.find

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class FindFavoriteMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(id: String): MusicResult? {
        return musicLiteRepository.findUserById(id)
    }
}