package com.example.musicapp.domain.usecase.room.find

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository

class FindSaveMusicFromSQLite(
    private val saveMusicRepository: SaveMusicRepository
) {
    suspend fun execute(firebaseId: String): SaveMusicEntity? {
        if (firebaseId.isEmpty()) {
            return null
        }

        return saveMusicRepository.getMusicById(firebaseId)
    }
}