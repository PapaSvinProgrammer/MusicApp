package com.example.musicapp.data.firebase.getAnother

import android.util.Log
import com.example.musicapp.data.constant.CollectionConst
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.GroupInfo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class GetGroupInfoImpl {
    suspend fun execute(groupId: String): GroupInfo? {
        val database = Firebase.firestore
        var result: GroupInfo? = null

        try {
            result = database.collection(CollectionConst.GROUP_INFO_COLLECTION)
                .document(groupId)
                .get()
                .await()
                .toObject<GroupInfo>()
        } catch (e: Exception) {
            Log.e(ErrorConst.FIREBASE_ERROR, "Error - GetGroupInfoImpl")
        }

        return result
    }
}