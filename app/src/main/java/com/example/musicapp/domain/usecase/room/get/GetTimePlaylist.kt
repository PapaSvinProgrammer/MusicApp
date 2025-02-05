package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.MusicLiteRepository

class GetTimePlaylist(
    private val musicLiteRepository: MusicLiteRepository
) {
    suspend fun getTime(playlistId: Long = 1L): Long {
        if (playlistId <= 0) {
            return -1
        }

        return 0
    }
}