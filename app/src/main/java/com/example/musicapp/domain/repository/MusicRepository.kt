package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Music

interface MusicRepository {
    suspend fun getMusicWithFilterOnGroup(): List<Music>
    suspend fun getMusicWithFilterOnGenre(): List<Music>
    suspend fun getMusicWithFilterOnAlbum(): List<Music>
    suspend fun getMusicWithFilterOnName(): List<Music>
    suspend fun getMusicAll(): List<Music>
}