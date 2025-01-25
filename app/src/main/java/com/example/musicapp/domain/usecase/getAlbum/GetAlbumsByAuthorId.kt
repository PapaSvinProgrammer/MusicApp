package com.example.musicapp.domain.usecase.getAlbum

import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class GetAlbumsByAuthorId(private val albumRepository: AlbumRepository) {
    suspend fun executeOrderRating(authorId: String): List<Album> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return albumRepository.getAlbumByAuthorIdOrderRating(authorId)
    }

    suspend fun executeOrderName(authorId: String): List<Album> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return albumRepository.getAlbumByAuthorIdOrderName(authorId)
    }

    suspend fun executeOrderDate(authorId: String): List<Album> {
        if (authorId.isEmpty()) {
            return listOf()
        }

        return albumRepository.getAlbumByAuthorIdOrderDate(authorId)
    }
}