package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class GetMusicsByAuthorId(private val musicRepository: MusicRepository) {
    suspend fun execute(authorId: String): List<Music> {
        return musicRepository.getMusicByAuthorId(authorId)
    }
}