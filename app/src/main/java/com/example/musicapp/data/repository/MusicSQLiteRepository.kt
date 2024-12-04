package com.example.musicapp.data.repository

import android.util.Log
import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

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

    override fun findUserById(firebaseId: String): MusicResult? {
        return musicDao.getMusicById(firebaseId)
    }


    override fun getAllMusic(): List<MusicResult?> {
        return musicDao.getAll()
    }

    override fun getAllAuthor(): List<AuthorEntity?> {
        return musicDao.getAllAuthor()
    }
}