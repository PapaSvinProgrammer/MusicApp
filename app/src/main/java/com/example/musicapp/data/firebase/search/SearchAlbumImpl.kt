package com.example.musicapp.data.firebase.search

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class SearchAlbumImpl {
    suspend fun execute(searchData: SearchData, limit: Long): List<Album> {
        val database = Firebase.firestore
        var result = listOf<Album>()

        try {
            result = database.collection(CollectionConst.ALBUM_COLLECTION)
                .orderBy(DocumentConst.ALBUM_NAME_FIELD)
                .startAt(searchData.text)
                .endAt(searchData.text + "\uf8ff")
                .limit(limit)
                .get()
                .await()
                .toObjects(Album::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "Error - SearchAlbumImpl")
        }

        return result
    }
}