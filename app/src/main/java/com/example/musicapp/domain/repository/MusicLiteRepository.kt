package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import kotlinx.coroutines.flow.Flow

interface MusicLiteRepository {
    suspend fun add(musicResult: MusicResult)
    suspend fun delete(id: String)

    suspend fun findMusicById(firebaseId: String): MusicResult?
    suspend fun findMusicByIdFromPlaylist(musicFirebaseId: String, playlistId: Long): MusicResult?

    suspend fun getMusicLimit(limit: Int): List<MusicResult>
    suspend fun getAuthorLimit(limit: Int): List<AuthorEntity>

    suspend fun getAllMusic(): List<MusicResult>
    fun getAllMusicFromPlaylist(playlistId: Long): Flow<List<MusicResult>>
    suspend fun getAllMusicFromPlaylist(playlistId: Long, limit: Int): List<MusicResult>
    suspend fun getAllAuthor(): List<AuthorEntity>

    suspend fun getCount(playlistId: Long): Int
    suspend fun getCount(): Int

    suspend fun getTime(playlistId: Long): Long

    suspend fun searchMusic(text: String): List<MusicResult>
    suspend fun searchAuthor(text: String): List<AuthorEntity>
}