package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Music

interface MusicRepository {
    suspend fun getRandomMusic(limit: Long): List<Music>
    suspend fun getMusicAll(): List<Music>

    suspend fun getMusicsByAlbumId(albumId: String): List<Music>

    suspend fun getMusicByAuthorIdOrderRating(authorId: String): List<Music>
    suspend fun getMusicByAuthorIdOrderName(authorId: String): List<Music>
    suspend fun getMusicByAuthorIdOrderAlbum(authorId: String): List<Music>

}