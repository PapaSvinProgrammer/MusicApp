package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.repository.PlaylistRepository

class AddPlaylistInSQLite(private val playlistRepository: PlaylistRepository) {
    suspend fun execute(name: String, image: String, id: Long = 1) {
        playlistRepository.add(
            PlaylistEntity(
                id = id,
                name = name,
                imageUrl = image
            )
        )
    }
}