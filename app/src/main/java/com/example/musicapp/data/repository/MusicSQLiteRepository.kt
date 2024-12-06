package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MusicSQLiteRepository(private val musicDao: MusicDao): MusicLiteRepository {
    override suspend fun add(
        albumEntity: AlbumEntity,
        authorEntity: AuthorEntity,
        favoriteMusicEntity: FavoriteMusicEntity
    ) {
        musicDao.insertAlbum(albumEntity)

        musicDao.insertAuthor(authorEntity)

        musicDao.insertMusic(favoriteMusicEntity)
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