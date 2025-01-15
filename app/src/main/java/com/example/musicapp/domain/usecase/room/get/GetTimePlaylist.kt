package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.MusicLiteRepository

class GetTimePlaylist(
    private val musicLiteRepository: MusicLiteRepository
) {
    suspend fun getTime(): Long {
        return getTime(1L)
    }

    suspend fun getTime(playlistId: Long): Long {
        if (playlistId <= 0) {
            return -1
        }

        return musicLiteRepository.getTime(playlistId.toString())
    }
}