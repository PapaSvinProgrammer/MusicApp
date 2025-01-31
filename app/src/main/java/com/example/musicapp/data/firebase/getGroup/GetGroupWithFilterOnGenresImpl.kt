package com.example.musicapp.data.firebase.getGroup

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GetGroupWithFilterOnGenresImpl {
    suspend fun execute(filterGenres: List<Int>): List<Group> {
        val database = Firebase.firestore
        var result = listOf<Group>()

        try {
            result = database
                .collection(CollectionConst.GROUP_COLLECTION)
                .whereArrayContainsAny(DocumentConst.GROUP_GENRE_FIELD, filterGenres)
                .get()
                .await()
                .toObjects(Group::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "GetGroupWithFilterOnGenresImpl - Error")
        }

        return result
    }
}