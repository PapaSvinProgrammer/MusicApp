package com.example.musicapp.data.firebase.getAnother

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.MusicText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class GetMusicTextById {
    suspend fun execute(musicId: String): MusicText? {
        val database = Firebase.firestore
        var result: MusicText? = null

        try {
            result = database
                .collection(CollectionConst.MUSIC_TEXT_COLLECTION)
                .document(musicId)
                .get()
                .await()
                .toObject<MusicText>()
        } catch (e: Exception) {
            Log.d(ErrorConst.FIREBASE_ERROR, "GetMusicTextById - Error")
        }

        return result
    }
}