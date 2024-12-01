package com.example.musicapp.data.repository

import com.example.musicapp.data.internalStorage.SaveInternalStorageImpl
import com.example.musicapp.domain.repository.MusicStorageRepository

const val AUDIO_DIRECTORY = "Musics"

class MusicInternalStorageRepository(
    private val saveInternalStorageImpl: SaveInternalStorageImpl
): MusicStorageRepository {
    override fun saveMusic(array: List<String>) {
        saveInternalStorageImpl.execute(array)
    }
}