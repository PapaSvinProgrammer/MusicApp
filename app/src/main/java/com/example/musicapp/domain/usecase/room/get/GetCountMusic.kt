package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.MusicLiteRepository

class GetCountMusic(
    private val musicLiteRepository: MusicLiteRepository
) {
    suspend fun getCount(): Int {
        return musicLiteRepository.getCount()
    }

    suspend fun getCount(playlistId: Long): Int {
        if (playlistId <= 0) {
            return -1
        }

        return musicLiteRepository.getCount(playlistId)
    }
}