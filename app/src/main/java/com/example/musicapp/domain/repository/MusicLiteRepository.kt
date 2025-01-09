package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult

interface MusicLiteRepository {
    suspend fun add(musicResult: MusicResult)
    suspend fun delete(id: String)
    suspend fun findUserById(firebaseId: String): MusicResult?
    suspend fun getMusicLimit(limit: String): List<MusicResult?>
    suspend fun getAuthorLimit(limit: String): List<AuthorEntity?>
    suspend fun getAllMusic(): List<MusicResult?>
    suspend fun getAllAuthor(): List<AuthorEntity?>
    suspend fun getCount(): Int
}