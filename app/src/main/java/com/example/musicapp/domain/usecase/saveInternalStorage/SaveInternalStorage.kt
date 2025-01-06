package com.example.musicapp.domain.usecase.saveInternalStorage

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicStorageRepository

class SaveInternalStorage(private val musicStorageRepository: MusicStorageRepository) {
    fun execute(music: Music) {
        musicStorageRepository.saveMusic(listOf(music))
    }

    fun execute(listMusic: List<Music>) {
        musicStorageRepository.saveMusic(listMusic)
    }
}