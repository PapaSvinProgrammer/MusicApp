package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.repository.SaveMusicRepository

class AddSaveMusicInSQLite(
    private val saveMusicRepository: SaveMusicRepository
) {
    suspend fun execute(music: SaveMusic) {
        saveMusicRepository.insert(
            music = convertData(music)
        )
    }

    private fun convertData(music: SaveMusic): SaveMusicEntity {
        return SaveMusicEntity(
            id = 0,
            firebaseId = music.id,
            uri = music.uri
        )
    }
}