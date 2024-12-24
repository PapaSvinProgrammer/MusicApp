package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class GetAlbumById(
    private val albumRepository: AlbumRepository
) {
    suspend fun execute(id: String): Album {
        return albumRepository.getAlbumById(id)
    }
}