package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class GetAlbum(private val albumRepository: AlbumRepository) {
    suspend fun getAlbumAll(): List<Album> {
        return albumRepository.getAlbumAll()
    }

    suspend fun getAlbumById(id: String): Album? {
        if (id.isEmpty()) {
            return null
        }

        return albumRepository.getAlbumById(id)
    }
}