package com.example.musicapp.data.repository

import com.example.musicapp.data.module.SaveInternalData
import com.example.musicapp.domain.repository.MusicStorageRepository

class MusicInternalStorageRepository: MusicStorageRepository {
    override fun saveMusic(saveInternalData: SaveInternalData) {
        TODO("Not yet implemented")
    }

    override fun readMusic(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun saveSomeMusic(array: List<SaveInternalData>) {
        TODO("Not yet implemented")
    }
}