package com.example.musicapp.data.firebase.getMusic

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetMusicsByFieldIdImpl {
    suspend fun execute(anyId: String, field: String): List<Music> {
        val database = Firebase.firestore
        var result = listOf<Music>()

        try {
            result = database
                .collection(CollectionConst.MUSIC_COLLECTION)
                .whereEqualTo(field, anyId)
                .get()
                .await()
                .toObjects(Music::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "GetMusicsByFieldIdImpl - Error")
        }

        return result
    }
}