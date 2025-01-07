package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import kotlinx.coroutines.flow.Flow

interface SaveMusicRepository {
    suspend fun insert(music: SaveMusicEntity)
    suspend fun delete(firebaseId: String)
    suspend fun getMusicById(firebaseId: String): SaveMusicEntity?
    fun getAll(): Flow<List<SaveMusicEntity?>>
}