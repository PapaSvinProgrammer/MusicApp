package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.repository.SaveMusicRepository

class AddSaveMusicInSQLite(
    private val saveMusicRepository: SaveMusicRepository
) {
    suspend fun execute(musicId: String) {
        saveMusicRepository.insert(
            music = convertData(musicId)
        )
    }

    private fun convertData(musicId: String): SaveMusicEntity {
        return SaveMusicEntity(
            id = 0,
            firebaseId = musicId
        )
    }
}