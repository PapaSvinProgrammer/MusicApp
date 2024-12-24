package com.example.musicapp.data.firebase.getGroup

import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.GroupConst
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetGroupByIdImpl {
    suspend fun execute(groupId: String): Group {
        val database = Firebase.firestore

        val document = database
            .collection(CollectionConst.GROUP_COLLECTION)
            .document(groupId)
            .get()
            .await()

        return Group(
            name = document[GroupConst.GROUP_NAME_FIELD].toString(),
            albums = arrayListOf(),
            compound = arrayListOf(),
            genre = document[GroupConst.GROUP_GENRE_FIELD].toString(),
            country = document[GroupConst.GROUP_COUNTRY_FIELD].toString(),
            musics = arrayListOf(),
            year = document[GroupConst.GROUP_YEARS_FIELD].toString(),
            image = document[GroupConst.GROUP_IMAGE_FIELD].toString()
        )
    }
}