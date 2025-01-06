package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.SaveMusicDao
import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository
import kotlinx.coroutines.flow.Flow

class SaveMusicInternalRepository(
    private val saveMusicDao: SaveMusicDao
): SaveMusicRepository {
    override suspend fun insert(music: SaveMusicEntity) {
        saveMusicDao.insert(music)
    }

    override suspend fun delete(firebaseId: String) {
        saveMusicDao.delete(firebaseId)
    }

    override fun getAll(): Flow<List<SaveMusicEntity?>> {
        return saveMusicDao.getAll()
    }
}