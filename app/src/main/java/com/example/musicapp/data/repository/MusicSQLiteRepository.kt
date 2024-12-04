package com.example.musicapp.data.repository

import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository

class MusicSQLiteRepository(
    private val musicDao: MusicDao
): MusicLiteRepository {
    override suspend fun add(music: Music) {
        musicDao.insertAlbum(
            AlbumEntity(
                firebaseId = music.album,
                name = "album_name",
                imageLow = music.imageLow,
                imageHigh = music.imageHigh
            )
        )

        musicDao.insertAuthor(
            AuthorEntity(
                firebaseId = "author_id",
                name = music.name
            )
        )

        musicDao.insertMusic(
            FavoriteMusicEntity(
                firebaseId = music.id,
                name = music.name,
                albumId = music.album,
                authorId = "author_id",
                url = music.url,
                saveUri = ""
            )
        )
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