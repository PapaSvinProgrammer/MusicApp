package com.example.musicapp.data.firebase.getMusic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.constant.DataConst
import com.example.musicapp.data.constant.MusicConst
import com.example.musicapp.domain.module.Music
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetMusicAllImpl {
    private val resultLiveData = MutableLiveData<ArrayList<Music>>()
    val result: LiveData<ArrayList<Music>> = resultLiveData

    fun execute() {
        val database = Firebase.firestore

        CoroutineScope(Dispatchers.Main).launch {
            database
                .collection(DataConst.MUSIC_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    val temp: ArrayList<Music> = ArrayList()

                    for (document in result) {
                        temp.add(
                            Music(
                                id = document.id,
                                group = document[MusicConst.MUSIC_GROUP_FIELD].toString(),
                                name = document[MusicConst.MUSIC_NAME_FIELD].toString(),
                                album = document[MusicConst.MUSIC_ALBUM_FIELD].toString(),
                                url = document[MusicConst.MUSIC_URL_FIELD].toString(),
                                image = document[MusicConst.MUSIC_IMAGE_FIELD].toString()
                            )
                        )
                    }

                    resultLiveData.value = temp
                }
                .addOnFailureListener { exception ->
                    Log.w("GetMusicAll", "Error get documents", exception)
                }
        }
    }
}