package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetTimePlaylist(
    private val playlistRepository: PlaylistRepository
) {
    fun getTime(playlistId: Long = 1L): Flow<Int> {
        if (playlistId <= 0L) {
            return flowOf(-1)
        }

        return playlistRepository.getTimePlaylist(playlistId)
    }
}