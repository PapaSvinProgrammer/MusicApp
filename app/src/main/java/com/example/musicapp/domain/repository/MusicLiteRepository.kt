package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import kotlinx.coroutines.flow.Flow

interface MusicLiteRepository {
    suspend fun add(musicResult: MusicResult, playlistId: Long)
    suspend fun delete(id: String, playlistId: Long)
    suspend fun findMusicById(firebaseId: String): MusicResult?

    fun getMusicLimit(limit: Int): Flow<List<MusicResult>>
    fun getAuthorLimit(limit: Int): Flow<List<AuthorEntity>>
    fun getAllMusic(): Flow<List<MusicResult>>
    fun getAllAuthor(): Flow<List<AuthorEntity>>

    suspend fun searchMusic(text: String): List<MusicResult>
    suspend fun searchAuthor(text: String): List<AuthorEntity>
}