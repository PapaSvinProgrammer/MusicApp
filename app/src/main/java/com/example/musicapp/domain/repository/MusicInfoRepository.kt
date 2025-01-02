package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.MusicInfo

interface MusicInfoRepository {
    suspend fun getInfoById(musicId: String): MusicInfo?
}