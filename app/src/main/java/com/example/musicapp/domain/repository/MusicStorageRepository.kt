package com.example.musicapp.domain.repository

interface MusicStorageRepository {
    fun saveMusic(array: List<String>)
}