package com.example.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository

class GroupRepositoryFirebase(
    private val getGroupAllImpl: GetGroupAllImpl,
    private val getGroupWithFilterOnGenresImpl: GetGroupWithFilterOnGenresImpl
): GroupRepository {
    override fun getGroupWithFilterOnName(): LiveData<ArrayList<Group>> {
        TODO("Not yet implemented")
    }

    override fun getGroupWithFilterOnGenre(filter: List<String>): LiveData<ArrayList<Group>> {
        getGroupWithFilterOnGenresImpl.execute(filter)
        return getGroupWithFilterOnGenresImpl.result
    }

    override fun getGroupAll(): LiveData<ArrayList<Group>> {
        getGroupAllImpl.execute()
        return getGroupAllImpl.result
    }
}