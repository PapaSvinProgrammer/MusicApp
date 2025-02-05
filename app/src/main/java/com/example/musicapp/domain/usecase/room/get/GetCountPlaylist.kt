package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetCountPlaylist(
    private val playlistRepository: PlaylistRepository
) {

    fun execute(): Flow<Int> {
        return playlistRepository.getCountPlaylist()
    }
}