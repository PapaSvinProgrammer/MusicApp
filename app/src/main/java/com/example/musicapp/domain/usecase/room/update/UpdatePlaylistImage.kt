package com.example.musicapp.domain.usecase.room.update

import com.example.musicapp.domain.repository.PlaylistRepository

class UpdatePlaylistImage(private val playlistRepository: PlaylistRepository) {
    suspend fun saveImage(url: String, id: Long) {
        if (id == -1L) return

        playlistRepository.saveImage(
            url = url,
            id = id.toString()
        )
    }

    suspend fun deleteImage(id: Long) {
        if (id == -1L) return

        playlistRepository.saveImage(
            url = "",
            id = id.toString()
        )
    }
}