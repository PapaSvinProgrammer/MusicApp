package com.example.musicapp.data.firebase.getAlbum

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Album
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetAlbumFilterImpl {
    suspend fun execute(
        anyId: String,
        field: String,
        filter: String,
        sort: Query.Direction = Query.Direction.DESCENDING
    ): List<Album> {
        val database = Firebase.firestore
        var result = listOf<Album>()

        try {
            result = database
                .collection(CollectionConst.ALBUM_COLLECTION)
                .orderBy(filter, sort)
                .whereEqualTo(field, anyId)
                .get()
                .await()
                .toObjects(Album::class.java)
        } catch (e: Exception) {
            Log.d("RRRR", e.message.toString())
            Log.e(ErrorConst.FIREBASE_ERROR, "GetAlbumByFieldIdImpl - Error")
        }

        return result
    }
}