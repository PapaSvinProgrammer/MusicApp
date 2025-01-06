package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class GetMusicsByAlbumId(private val musicRepository: MusicRepository) {
    suspend fun execute(albumId: String): List<Music> {
        if (albumId.isEmpty()) {
            return listOf()
        }

        return musicRepository.getMusicsByAlbumId(albumId)
    }
}