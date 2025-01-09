package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.saveMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository

class AddSaveMusicInSQLite(
    private val saveMusicRepository: SaveMusicRepository
) {
    suspend fun execute(musicId: String) {
        if (musicId.isEmpty()) {
            return
        }

        saveMusicRepository.add(
            SaveMusicEntity(
                id = 0,
                musicId = musicId
            )
        )
    }
}