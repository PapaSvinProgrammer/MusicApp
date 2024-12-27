package com.example.musicapp.data.firebase.getGroup

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.domain.module.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class GetGroupByIdImpl {
    suspend fun execute(groupId: String): Group? {
        val database = Firebase.firestore
        var result: Group? = null

        try {
            result = database
                .collection(CollectionConst.GROUP_COLLECTION)
                .document(groupId)
                .get()
                .await()
                .toObject<Group>()
        } catch (e: Exception) {
            Log.e("FirebaseError", "GetGroupByIdImpl - Error")
        }

        return result
    }
}