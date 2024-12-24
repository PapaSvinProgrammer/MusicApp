package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Album

interface AlbumRepository {
    suspend fun getAlbumWithFilterOnGenre(): List<Album>
    suspend fun getAlbumAll(): List<Album>
    suspend fun getAlbumById(id: String): Album
}