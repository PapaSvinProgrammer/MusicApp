package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class GetMusicAll(private val musicRepository: MusicRepository) {
    suspend fun execute(): List<Music> {
        return musicRepository.getMusicAll()
    }
}