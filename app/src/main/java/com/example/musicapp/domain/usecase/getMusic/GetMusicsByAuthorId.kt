package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class GetMusicsByAuthorId(private val musicRepository: MusicRepository) {
    suspend fun executeOrderRating(authorId: String): List<Music> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return musicRepository.getMusicByAuthorIdOrderRating(authorId)
    }

    suspend fun executeOrderRating(authorId: String, limit: Long): List<Music> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return musicRepository.getMusicByAuthorIdOrderRating(authorId, limit)
    }

    suspend fun executeOrderAlbum(authorId: String): List<Music> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return musicRepository.getMusicByAuthorIdOrderAlbum(authorId)
    }

    suspend fun executeOrderName(authorId: String): List<Music> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return musicRepository.getMusicByAuthorIdOrderName(authorId)
    }
}