package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MusicSQLiteRepository(private val musicDao: MusicDao): MusicLiteRepository {
    override suspend fun add(musicResult: MusicResult) {
        musicDao.insertAlbum(musicResult.albumEntity)

        musicDao.insertAuthor(musicResult.authorEntity)

        musicDao.insertMusic(musicResult.musicEntity)
    }

    override suspend fun delete(id: String) {
        musicDao.deleteMusicById(id)
    }

    override suspend fun findUserById(firebaseId: String): MusicResult? {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getMusicById(firebaseId)
        }

        return job.await()
    }

    override suspend fun getMusicLimit(limit: String): List<MusicResult?> {
       val job = CoroutineScope(Dispatchers.IO).async {
           musicDao.getMusicLimit(limit)
       }

        return job.await()
    }

    override suspend fun getAuthorLimit(limit: String): List<AuthorEntity?> {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getAuthorLimit(limit)
        }

        return job.await()
    }


    override suspend fun getAllMusic(): List<MusicResult?> {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getAll()
        }

        return job.await()
    }

    override suspend fun getAllAuthor(): List<AuthorEntity?> {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getAllAuthor()
        }

        return job.await()
    }
}