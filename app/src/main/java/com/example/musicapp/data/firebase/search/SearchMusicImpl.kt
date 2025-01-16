package com.example.musicapp.data.firebase.search

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class SearchMusicImpl {
    suspend fun execute(
        searchData: SearchData,
        limit: Long,
        field: String
    ): List<Music> {
        val database = Firebase.firestore
        var result: List<Music> = listOf()

        try {
            result = database.collection(CollectionConst.MUSIC_COLLECTION)
                .orderBy(field)
                .startAt(searchData.text)
                .endAt(searchData.text + "\uf8ff")
                .limit(limit)
                .get()
                .await()
                .toObjects(Music::class.java)
        }
        catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "Error - SearchImpl")
        }

        return result
    }
}