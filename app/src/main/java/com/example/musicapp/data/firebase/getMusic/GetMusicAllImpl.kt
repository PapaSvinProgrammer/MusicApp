package com.example.musicapp.data.firebase.getMusic

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetMusicAllImpl {
    suspend fun execute(): List<Music> {
        var result = listOf<Music>()
        val database = Firebase.firestore

        try {
            result = database
                .collection(CollectionConst.MUSIC_COLLECTION)
                .get()
                .await()
                .toObjects(Music::class.java)
        }
        catch (e: Exception) {
            Log.e("FirebaseError", "GetMusicAllImpl - Error")
        }

        return result
    }
}