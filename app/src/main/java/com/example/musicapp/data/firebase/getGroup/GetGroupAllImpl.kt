package com.example.musicapp.data.firebase.getGroup

import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.GroupConst
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetGroupAllImpl {
    suspend fun execute(): List<Group> {
        val database = Firebase.firestore
        val result = ArrayList<Group>()

        database
            .collection(CollectionConst.GROUP_COLLECTION)
            .get()
            .await()
            .documents
            .forEach { document ->
                result.add(
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

        return result
    }
}