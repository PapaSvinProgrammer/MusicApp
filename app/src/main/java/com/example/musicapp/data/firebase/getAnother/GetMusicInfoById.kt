package com.example.musicapp.data.firebase.getAnother

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.MusicInfo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class GetMusicInfoById {
    suspend fun execute(musicId: String): MusicInfo? {
        val database = Firebase.firestore
        var result: MusicInfo? = null

       try {
            result = database
                .collection(CollectionConst.MUSIC_INFO_COLLECTION)
                .document(musicId)
                .get()
                .await()
                .toObject<MusicInfo>()

       } catch (e: Exception) {
            Log.d(ErrorConst.FIREBASE_ERROR, "GetMusicInfoById - Error")
       }

        return result
    }
}