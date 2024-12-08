package com.example.musicapp.data.firebase.getMusic

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.MusicConst
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetMusicAllImpl {
    suspend fun execute(): List<Music> {
        val result = arrayListOf<Music>()
        val database = Firebase.firestore

        try {
            database
                .collection(CollectionConst.MUSIC_COLLECTION)
                .get()
                .await()
                .documents
                .forEach { document->
                    result.add(
                        Music(
                            id = document.id,
                            group = document[MusicConst.MUSIC_GROUP_FIELD].toString(),
                            name = document[MusicConst.MUSIC_NAME_FIELD].toString(),
                            album = document[MusicConst.MUSIC_ALBUM_FIELD].toString(),
                            url = document[MusicConst.MUSIC_URL_FIELD].toString(),
                            imageLow = document[MusicConst.MUSIC_IMAGE_LOW_FIELD].toString(),
                            imageHigh = document[MusicConst.MUSIC_IMAGE_HIGH_FIELD].toString(),
                            groupId = document[MusicConst.MUSIC_GROUP_ID_FIELD].toString(),
                            imageGroup = document[MusicConst.MUSIC_IMAGE_GROUP_FIELD].toString()
                        )
                    )
                }
        }
        catch (e: Exception) {
            Log.e("FirebaseError", "GetMusicAllImpl - Error")
        }

        return result
    }
}