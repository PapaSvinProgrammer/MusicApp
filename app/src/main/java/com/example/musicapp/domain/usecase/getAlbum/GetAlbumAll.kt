package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class GetAlbumAll(private val albumRepository: AlbumRepository) {
    suspend fun execute(): List<Album> {
        return albumRepository.getAlbumAll()
    }
}