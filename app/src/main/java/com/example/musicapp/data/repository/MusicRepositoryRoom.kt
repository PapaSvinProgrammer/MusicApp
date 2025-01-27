package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MusicRepositoryRoom(private val musicDao: MusicDao): MusicLiteRepository {

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

    override suspend fun getMusicLimit(limit: Int): List<MusicResult> {
       val job = CoroutineScope(Dispatchers.IO).async {
           musicDao.getMusicLimit(limit)
       }

        return job.await()
    }

    override suspend fun getAuthorLimit(limit: Int): List<AuthorEntity> {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getAuthorLimit(limit)
        }

        return job.await()
    }

    override suspend fun getAllMusic(): List<MusicResult> {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getAll()
        }

        return job.await()
    }

    override suspend fun getAllAuthor(): List<AuthorEntity> {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getAllAuthor()
        }

        return job.await()
    }

    override suspend fun getCount(playlistId: Long): Int {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getCount(playlistId)
        }

        return job.await()
    }

    override suspend fun getCount(): Int {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getCount(1L)
        }

        return job.await()
    }

    override suspend fun getTime(playlistId: Long): Long {
        val job = CoroutineScope(Dispatchers.IO).async {
            musicDao.getTime(playlistId)
        }

        return job.await()
    }

    override suspend fun searchMusic(text: String): List<MusicResult> {
        return CoroutineScope(Dispatchers.IO).async {
            musicDao.searchMusic("%$text%")
        }.await()
    }

    override suspend fun searchAuthor(text: String): List<AuthorEntity> {
        return CoroutineScope(Dispatchers.IO).async {
            musicDao.searchAuthor("%$text%")
        }.await()
    }
}