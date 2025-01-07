package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.SaveMusicDao
import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

    override suspend fun getMusicById(firebaseId: String): SaveMusicEntity? {
        return CoroutineScope(Dispatchers.IO).async {
            saveMusicDao.getMusicById(firebaseId)
        }.await()
    }

    override fun getAll(): Flow<List<SaveMusicEntity?>> {
        return saveMusicDao.getAll()
    }
}