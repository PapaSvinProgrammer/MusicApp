package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository
import kotlinx.coroutines.flow.Flow

class GetSaveMusicFromSQLite(
    private val saveMusicRepository: SaveMusicRepository
) {
    fun execute(): Flow<List<SaveMusicEntity?>> {
        return saveMusicRepository.getAll()
    }
}