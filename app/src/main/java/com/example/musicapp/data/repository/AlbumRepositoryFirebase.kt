package com.example.musicapp.data.repository

import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.firebase.getAlbum.GetAlbumFilterImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByIdImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumsAllImpl
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository
import com.google.firebase.firestore.Query

class AlbumRepositoryFirebase(
    private val getAlbumByIdImpl: GetAlbumByIdImpl,
    private val getAlbumFilterImpl: GetAlbumFilterImpl,
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

    override suspend fun getAlbumByAuthorIdOrderRating(authorId: String): List<Album> {
        //TODO
        return getAlbumFilterImpl.execute(
            anyId = authorId,
            field = DocumentConst.ALBUM_GROUP_FIELD,
            filter = DocumentConst.ALBUM_DATE_FIELD
        )
    }

    override suspend fun getAlbumByAuthorIdOrderName(authorId: String): List<Album> {
        return getAlbumFilterImpl.execute(
            anyId = authorId,
            field = DocumentConst.ALBUM_GROUP_FIELD,
            filter = DocumentConst.ALBUM_NAME_FIELD,
            sort = Query.Direction.ASCENDING
        )
    }

    override suspend fun getAlbumByAuthorIdOrderDate(authorId: String): List<Album> {
        return getAlbumFilterImpl.execute(
            anyId = authorId,
            field = DocumentConst.ALBUM_GROUP_FIELD,
            filter = DocumentConst.ALBUM_DATE_FIELD
        )
    }
}