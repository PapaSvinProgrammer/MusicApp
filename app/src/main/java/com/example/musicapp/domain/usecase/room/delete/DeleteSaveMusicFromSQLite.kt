package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.domain.repository.SaveMusicRepository

class DeleteSaveMusicFromSQLite(
    private val saveMusicRepository: SaveMusicRepository
) {
    suspend fun execute(firebaseId: String) {
        saveMusicRepository.delete(firebaseId)
    }
}