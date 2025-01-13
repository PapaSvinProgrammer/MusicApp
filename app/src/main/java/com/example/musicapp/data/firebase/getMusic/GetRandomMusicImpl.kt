package com.example.musicapp.data.firebase.getMusic

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetRandomMusicImpl {
    suspend fun execute(limit: Long): List<Music> {
        var result = listOf<Music>()

        val database = Firebase.firestore
        val collection = database.collection(CollectionConst.MUSIC_COLLECTION)

        val randomKey = collection.document().id

        try {
            result = generateGreaterThan(randomKey, collection, limit)

            if (result.size <= limit) {
                return result
            }

            result = generateLessThan(randomKey, collection, limit)

            if (result.size <= limit) {
                return result
            }
        } catch (e: Exception) {
            Log.d(ErrorConst.FIREBASE_ERROR, "Error - GetRandomMusicImpl")
        }

        return result
    }

    private suspend fun generateGreaterThan(
        randomKey: String,
        collection: CollectionReference,
        limit: Long
    ): List<Music> {
        return collection
            .whereGreaterThanOrEqualTo("__name__", randomKey)
            .limit(limit)
            .get()
            .await()
            .toObjects(Music::class.java)
    }

    private suspend fun generateLessThan(
        randomKey: String,
        collection: CollectionReference,
        limit: Long
    ): List<Music> {
        return collection
            .whereLessThan("__name__", randomKey)
            .limit(limit)
            .get()
            .await()
            .toObjects(Music::class.java)
    }
}