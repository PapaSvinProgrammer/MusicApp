package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.PlaylistRepository

class GetCountPlaylist(
    private val playlistRepository: PlaylistRepository
) {

    suspend fun execute(): Int {
        return playlistRepository.getCount()
    }
}