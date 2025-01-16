package com.example.musicapp.data.firebase.search

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class SearchAllImpl {
    suspend fun execute(searchData: SearchData, limit: Long): List<Music> {
        val database = Firebase.firestore

        var resultMusic = listOf<Music>()
        var resultAlbum = listOf<Music>()
        var resultGroup = listOf<Music>()

        try {
            resultMusic = database.collection(CollectionConst.MUSIC_COLLECTION)
                .orderBy(DocumentConst.MUSIC_NAME_FIELD)
                .startAt(searchData.text)
                .endAt(searchData.text + "\uf8ff")
                .limit(limit)
                .get()
                .await()
                .toObjects(Music::class.java)

            resultAlbum = database.collection(CollectionConst.MUSIC_COLLECTION)
                .orderBy(DocumentConst.MUSIC_ALBUM_NAME_FIELD)
                .startAt(searchData.text)
                .endAt(searchData.text + "\uf8ff")
                .limit(limit)
                .get()
                .await()
                .toObjects(Music::class.java)

            resultGroup = database.collection(CollectionConst.MUSIC_COLLECTION)
                .orderBy(DocumentConst.MUSIC_GROUP_NAME_FIELD)
                .startAt(searchData.text)
                .endAt(searchData.text + "\uf8ff")
                .limit(limit)
                .get()
                .await()
                .toObjects(Music::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "Error - SearchAllImpl")
        }

        return (resultMusic + resultGroup + resultAlbum).toSet().toList()
    }
}