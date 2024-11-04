package com.example.musicapp.data.firebase.getGroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.constant.DataConst
import com.example.musicapp.data.constant.GroupConst
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetGroupAllImpl {
    private val resultLiveData = MutableLiveData<ArrayList<Group>>()
    val result: LiveData<ArrayList<Group>> = resultLiveData

    fun execute() {
        val database = Firebase.firestore

        CoroutineScope(Dispatchers.Main).launch {
            database
                .collection(DataConst.GROUP_COLLECTION)
                .get()
                .addOnSuccessListener { result ->
                    val temp = ArrayList<Group>()

                    for (document in result) {
                        temp.add(
                            Group(
                                id = document.id,
                                albums = document[GroupConst.GROUP_ALBUM_FIELD] as ArrayList<String>,
                                compound = document[GroupConst.GROUP_COMPOUND_FIELD] as ArrayList<String>,
                                genres = document[GroupConst.GROUP_GENRES_FIELD] as ArrayList<String>,
                                country = document[GroupConst.GROUP_COUNTRY_FIELD].toString(),
                                musics = document[GroupConst.GROUP_MUSICS_FIELD] as ArrayList<String>,
                                year = document[GroupConst.GROUP_YEARS_FIELD].toString(),
                                image = document[GroupConst.GROUP_IMAGE_FIELD].toString()
                            )
                        )
                    }

                    resultLiveData.value = temp
                }
                .addOnFailureListener { exception ->
                    Log.w("GetGroupAll", "Error get documents", exception)
                }
        }
    }
}