package com.example.musicapp.data.firebase.search

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class SearchGroupImpl {
    suspend fun execute(searchData: SearchData, limit: Long): List<Group> {
        val database = Firebase.firestore
        var result = listOf<Group>()

        try {
            result = database.collection(CollectionConst.GROUP_COLLECTION)
                .orderBy(DocumentConst.GROUP_NAME_FIELD)
                .startAt(searchData.text)
                .endAt(searchData.text + "\uf8ff")
                .limit(limit)
                .get()
                .await()
                .toObjects(Group::class.java)
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "Error - SearchGroupImpl")
        }

        return result
    }
}