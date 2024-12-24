package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.GetAlbumByIdImpl
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class AlbumRepositoryFirebase(
    private val getAlbumByIdImpl: GetAlbumByIdImpl
): AlbumRepository {
    override suspend fun getAlbumWithFilterOnGenre(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumAll(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumById(id: String): Album {
        return getAlbumByIdImpl.execute(id)
    }
}