package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.saveMusic.SaveMusicEntity

interface SaveMusicRepository {
    suspend fun add(item: SaveMusicEntity)
    suspend fun delete(musicId: String)
}