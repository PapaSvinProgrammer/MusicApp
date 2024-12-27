package com.example.musicapp.data.repository

import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByFieldIdImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByIdImpl
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class AlbumRepositoryFirebase(
    private val getAlbumByIdImpl: GetAlbumByIdImpl,
    private val getAlbumByFieldIdImpl: GetAlbumByFieldIdImpl
): AlbumRepository {
    override suspend fun getAlbumWithFilterOnGenre(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumAll(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumById(id: String): Album? {
        return getAlbumByIdImpl.execute(id)
    }

    override suspend fun getAlbumByAuthorId(authorId: String): List<Album> {
        return getAlbumByFieldIdImpl.execute(
            anyId = authorId,
            field = DocumentConst.ALBUM_GROUP_FIELD
        )
    }
}