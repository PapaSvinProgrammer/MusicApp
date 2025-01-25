package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Album

interface AlbumRepository {
    suspend fun getAlbumWithFilterOnGenre(): List<Album>
    suspend fun getAlbumAll(): List<Album>
    suspend fun getAlbumById(id: String): Album?

    suspend fun getAlbumByAuthorIdOrderRating(authorId: String): List<Album>
    suspend fun getAlbumByAuthorIdOrderName(authorId: String): List<Album>
    suspend fun getAlbumByAuthorIdOrderDate(authorId: String): List<Album>
}