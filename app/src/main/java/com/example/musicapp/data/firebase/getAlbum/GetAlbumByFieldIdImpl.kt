package com.example.musicapp.data.firebase.getAlbum

import com.example.musicapp.data.constant.AlbumConst
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.domain.module.Album
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetAlbumByFieldIdImpl {
    suspend fun execute(anyId: String, field: String): List<Album> {
        val result = arrayListOf<Album>()
        val database = Firebase.firestore

        database
            .collection(CollectionConst.ALBUM_COLLECTION)
            .whereEqualTo(field, anyId)
            .get()
            .await()
            .documents
            .forEach { document ->
                result.add(
                    Album(
                        id = document.id,
                        date = document.getString(AlbumConst.ALBUM_DATE_FIELD).toString(),
                        genre = document[AlbumConst.ALBUM_GENRE_FIELD].toString(),
                        image = document[AlbumConst.ALBUM_IMAGE_FIELD].toString(),
                        name = document[AlbumConst.ALBUM_NAME_FIELD].toString(),
                        time = document[AlbumConst.ALBUM_TIME_FIELD].toString()
                    )
                )
            }

        return result
    }
}