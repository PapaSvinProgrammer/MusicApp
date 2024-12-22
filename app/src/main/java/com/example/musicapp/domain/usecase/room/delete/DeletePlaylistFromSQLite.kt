package com.example.musicapp.domain.usecase.room.delete

import com.example.musicapp.domain.repository.PlaylistRepository

class DeletePlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    suspend fun execute(id: Long) {
        if (id == -1L) return

        playlistRepository.delete(id.toString())
    }
}