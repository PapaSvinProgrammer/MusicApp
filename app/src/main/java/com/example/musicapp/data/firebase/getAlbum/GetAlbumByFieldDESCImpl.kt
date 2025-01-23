package com.example.musicapp.data.firebase.getAlbum

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Album
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetAlbumByFieldDESCImpl {
    suspend fun execute(anyId: String, field: String): List<Album> {
        val database = Firebase.firestore
        var result = listOf<Album>()

        try {
            result = database
                .collection(CollectionConst.ALBUM_COLLECTION)
                .orderBy(DocumentConst.ALBUM_DATE_FIELD, Query.Direction.DESCENDING)
                .whereEqualTo(field, anyId)
                .get()
                .await()
                .toObjects(Album::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "GetAlbumByFieldIdImpl - Error")
        }

        return result
    }
}