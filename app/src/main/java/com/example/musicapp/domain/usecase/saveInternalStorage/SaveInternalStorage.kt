package com.example.musicapp.domain.usecase.saveInternalStorage

import com.example.musicapp.domain.repository.MusicStorageRepository

class SaveInternalStorage(private val musicStorageRepository: MusicStorageRepository) {
    fun execute() {
        musicStorageRepository.saveMusic(arrayListOf("https://firebasestorage.googleapis.com/v0/b/communication-d9255.appspot.com/o/music_audio%2Fpornofilmy-chuzhoe-gore-mp3.mp3?alt=media&token=407bbfcc-d4b0-4d73-952f-1d10b12fa6a2"))
    }
}