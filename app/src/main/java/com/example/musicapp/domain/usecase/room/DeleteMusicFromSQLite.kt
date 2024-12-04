package com.example.musicapp.domain.usecase.room

import com.example.musicapp.domain.repository.MusicLiteRepository

class DeleteMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(id: String) {
        musicLiteRepository.delete(id)
    }
}