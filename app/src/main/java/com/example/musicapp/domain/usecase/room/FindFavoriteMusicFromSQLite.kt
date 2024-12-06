package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class FindFavoriteMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(id: String): MusicResult? {
        return musicLiteRepository.findUserById(id)
    }
}