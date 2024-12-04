package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class GetAllMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    fun execute(): List<MusicResult?> {
        return musicLiteRepository.getAllMusic()
    }
}