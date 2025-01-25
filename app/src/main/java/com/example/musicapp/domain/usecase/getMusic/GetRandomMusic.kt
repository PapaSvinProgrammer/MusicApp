package com.example.musicapp.domain.usecase.getMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

private const val DEFAULT_COUNT_SINGLE = 1L

class GetRandomMusic(
    private val musicRepository: MusicRepository
) {
    suspend fun getMusicSingle(): Music? {
        val result = musicRepository.getRandomMusic(DEFAULT_COUNT_SINGLE)

        if (result.isNotEmpty()) {
            return result.first()
        }

        return null
    }

    suspend fun getMusics(limit: Long): List<Music> {
        if (limit <= 0L) {
            return listOf()
        }

        return musicRepository.getRandomMusic(limit)
    }
}