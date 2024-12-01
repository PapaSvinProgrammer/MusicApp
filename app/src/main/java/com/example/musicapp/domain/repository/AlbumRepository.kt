package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Album

interface AlbumRepository {
    fun getAlbumWithFilterOnGenre(): List<Album>
    fun getAlbumAll(): List<Album>
}