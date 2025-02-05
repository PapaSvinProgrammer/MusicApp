package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.domain.repository.MusicLiteRepository

class DeleteMusicFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(id: String, playlistId: Long = 1) {
        if (id.isEmpty() || playlistId <= 0L) {
            return
        }

        musicLiteRepository.delete(id, playlistId)
    }
}