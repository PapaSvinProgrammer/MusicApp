package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult

interface MusicLiteRepository {
    suspend fun add(albumEntity: AlbumEntity, authorEntity: AuthorEntity, favoriteMusicEntity: FavoriteMusicEntity)
    suspend fun delete(id: String)
    fun findUserById(firebaseId: String): MusicResult?
    fun getAllMusic(): List<MusicResult?>
    fun getAllAuthor(): List<AuthorEntity?>
}