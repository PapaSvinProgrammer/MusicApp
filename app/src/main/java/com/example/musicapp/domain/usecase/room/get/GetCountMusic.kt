package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCountMusic(
    private val playlistRepository: PlaylistRepository
) {
    fun getCountMusicInPlaylist(playlistId: Long): Flow<Int> {
        if (playlistId <= 0) {
            return flow { emit(-1) }
        }

        return playlistRepository.getCountMusicInPlaylist(playlistId)
    }
}