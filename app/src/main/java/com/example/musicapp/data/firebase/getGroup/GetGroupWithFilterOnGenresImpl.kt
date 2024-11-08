package com.example.musicapp.data.firebase.getGroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.GroupConst
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class GetGroupWithFilterOnGenresImpl {
    private val resultLiveData = MutableLiveData<ArrayList<Group>>()
    val result: LiveData<ArrayList<Group>> = resultLiveData

    fun execute(filterGenres: List<String>) {
        val database = Firebase.firestore

        database
            .collection(CollectionConst.GROUP_COLLECTION)
            .whereIn(GroupConst.GROUP_GENRE_FIELD, filterGenres)
            .get()
            .addOnSuccessListener { result->
                val temp = ArrayList<Group>()

                for (document in result) {
                    temp.add(
                        Group(
                            name = document[GroupConst.GROUP_NAME_FIELD].toString(),
                            albums = document[GroupConst.GROUP_ALBUM_FIELD] as ArrayList<String>,
                            compound = document[GroupConst.GROUP_COMPOUND_FIELD] as ArrayList<String>,
                            genre = document[GroupConst.GROUP_GENRE_FIELD].toString(),
                            country = document[GroupConst.GROUP_COUNTRY_FIELD].toString(),
                            musics = document[GroupConst.GROUP_MUSICS_FIELD] as ArrayList<String>,
                            year = document[GroupConst.GROUP_YEARS_FIELD].toString(),
                            image = document[GroupConst.GROUP_IMAGE_FIELD].toString()
                        )
                    )
                }

                resultLiveData.value = temp
            }
            .addOnFailureListener {
                Log.e("GetGroupWithFilterOnGenres", it.message.toString())
            }
    }
}