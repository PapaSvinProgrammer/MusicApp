package com.example.musicapp.data.repository

import com.example.musicapp.data.internalStorage.SaveInternalStorageImpl
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicStorageRepository

class MusicInternalStorageRepository(
    private val saveInternalStorageImpl: SaveInternalStorageImpl
): MusicStorageRepository {
    override fun saveMusic(array: List<Music>) {
        saveInternalStorageImpl.execute(array)
    }
}