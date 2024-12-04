package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult
import com.example.musicapp.domain.module.Music

interface MusicLiteRepository {
    suspend fun add(music: Music)
    suspend fun delete(id: String)
    fun findUserById(firebaseId: String): MusicResult?
    fun getAllMusic(): List<MusicResult?>
    fun getAllAuthor(): List<AuthorEntity?>
}