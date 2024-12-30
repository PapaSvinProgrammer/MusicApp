package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.MusicText

interface MusicTextRepository {
    suspend fun getTextById(musicId: String): List<MusicText>
}