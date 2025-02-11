package com.example.musicapp.data.firebase.getMusic

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetMusicsFilterLimitImpl {
    suspend fun execute(
        id: String,
        field: String,
        filter: String,
        sort: Query.Direction = Query.Direction.ASCENDING,
        limit: Long
    ): List<Music> {
        val database = Firebase.firestore
        var result = listOf<Music>()

        try {
            result = database
                .collection(CollectionConst.MUSIC_COLLECTION)
                .orderBy(filter, sort)
                .whereEqualTo(field, id)
                .limit(limit)
                .get()
                .await()
                .toObjects(Music::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "Error - GetMusicsFilterImpl")
        }

        return result
    }
}