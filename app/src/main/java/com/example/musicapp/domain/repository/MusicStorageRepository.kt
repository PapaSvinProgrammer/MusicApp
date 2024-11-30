package com.example.musicapp.domain.repository

import com.example.musicapp.data.module.SaveInternalData


interface MusicStorageRepository {
    fun saveMusic(saveInternalData: SaveInternalData)
    fun readMusic(): ByteArray
    fun saveSomeMusic(array: List<SaveInternalData>)
}