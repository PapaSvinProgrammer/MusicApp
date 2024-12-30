package com.example.musicapp.data.firebase.getMusicText

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.domain.module.MusicText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class GetMusicTextById {
    suspend fun execute(musicId: String): List<MusicText> {
        val database = Firebase.firestore
        var result = listOf<MusicText>()

        try {
            result = database
                .collection(CollectionConst.MUSIC_TEXT_COLLECTION)
                .whereEqualTo("musicId", musicId)
                .get()
                .await()
                .toObjects<MusicText>()
        } catch (e: Exception) {
            Log.d("FirebaseError", "GetMusicTextById - Error")
        }

        return result
    }
}