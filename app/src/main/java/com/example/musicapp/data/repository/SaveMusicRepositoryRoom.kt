package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.SaveMusicDao
import com.example.musicapp.data.room.saveMusic.SaveMusicEntity
import com.example.musicapp.domain.repository.SaveMusicRepository

class SaveMusicRepositoryRoom(
    private val saveMusicDao: SaveMusicDao
): SaveMusicRepository {
    override suspend fun add(item: SaveMusicEntity) {
        saveMusicDao.insert(item)
    }

    override suspend fun delete(musicId: String) {
        saveMusicDao.delete(musicId)
    }
}