package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val ERROR_RESULT = -1L

class GetCountMusic(
    private val playlistRepository: PlaylistRepository
) {
    fun getCountMusicInPlaylist(playlistId: Long): Flow<Int> {
        if (playlistId <= 0) {
            return flow { ERROR_RESULT }
        }

        return playlistRepository.getCountMusicInPlaylist(playlistId)
    }
}