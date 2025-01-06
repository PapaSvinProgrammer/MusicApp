package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Music

interface MusicStorageRepository {
    fun saveMusic(array: List<Music>)
}