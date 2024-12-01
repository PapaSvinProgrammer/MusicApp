package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository

class GroupRepositoryFirebase(
    private val getGroupAllImpl: GetGroupAllImpl,
    private val getGroupWithFilterOnGenresImpl: GetGroupWithFilterOnGenresImpl
): GroupRepository {
    override suspend fun getGroupWithFilterOnName(): List<Group> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupWithFilterOnGenre(filter: List<String>): List<Group> {
        return getGroupWithFilterOnGenresImpl.execute(filter)
    }

    override suspend fun getGroupAll(): List<Group> {
        return getGroupAllImpl.execute()
    }
}