package com.example.musicapp.domain.usecase.room.update

import com.example.musicapp.domain.repository.PlaylistRepository

class UpdatePlaylistName(private val playlistRepository: PlaylistRepository) {
    suspend fun execute(name: String, id: Long) {
        if (id <= 0L) return

        playlistRepository.saveName(
            name = name,
            id = id.toString()
        )
    }
}