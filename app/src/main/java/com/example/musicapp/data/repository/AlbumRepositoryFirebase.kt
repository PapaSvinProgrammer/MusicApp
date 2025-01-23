package com.example.musicapp.data.repository

import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByFieldDESCImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByIdImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumsAllImpl
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class AlbumRepositoryFirebase(
    private val getAlbumByIdImpl: GetAlbumByIdImpl,
    private val getAlbumByFieldDESCImpl: GetAlbumByFieldDESCImpl,
    private val getAlbumsAllImpl: GetAlbumsAllImpl
): AlbumRepository {
    override suspend fun getAlbumWithFilterOnGenre(): List<Album> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumAll(): List<Album> {
        return getAlbumsAllImpl.execute()
    }

    override suspend fun getAlbumById(id: String): Album? {
        return getAlbumByIdImpl.execute(id)
    }

    override suspend fun getAlbumByAuthorId(authorId: String): List<Album> {
        return getAlbumByFieldDESCImpl.execute(
            anyId = authorId,
            field = DocumentConst.ALBUM_GROUP_FIELD
        )
    }
}