package com.example.musicapp.data.firebase

import android.util.Log
import com.example.musicapp.data.constant.AlbumConst
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.domain.module.Album
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetAlbumByIdImpl {
    suspend fun execute(id: String): Album {
        val database = Firebase.firestore

        val document = database
            .collection(CollectionConst.ALBUM_COLLECTION)
            .document(id)
            .get()
            .await()

        return Album(
            id = document.id,
            date = document.getString(AlbumConst.ALBUM_GENRE_FIELD).toString(),
            genre = document[AlbumConst.ALBUM_GENRE_FIELD].toString(),
            image = document[AlbumConst.ALBUM_IMAGE_FIELD].toString(),
            name = document[AlbumConst.ALBUM_NAME_FIELD].toString(),
            time = document[AlbumConst.ALBUM_TIME_FIELD].toString()
        )
    }
}