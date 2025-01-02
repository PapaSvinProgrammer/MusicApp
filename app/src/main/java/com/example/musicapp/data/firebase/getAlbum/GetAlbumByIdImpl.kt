package com.example.musicapp.data.firebase.getAlbum

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Album
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class GetAlbumByIdImpl {
    suspend fun execute(id: String): Album? {
        val database = Firebase.firestore
        var result: Album? = null

        try {
            result = database
                .collection(CollectionConst.ALBUM_COLLECTION)
                .document(id)
                .get()
                .await()
                .toObject<Album>()
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "GetAlbumByIdImpl - Error")
        }

        return result
    }
}