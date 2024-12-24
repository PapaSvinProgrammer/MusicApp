package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class GetAlbumsByAuthorId(private val albumRepository: AlbumRepository) {
    suspend fun execute(authorId: String): List<Album> {
        return albumRepository.getAlbumByAuthorId(authorId)
    }
}