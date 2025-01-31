package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.getAnother.GetGroupInfoImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupByIdImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.GroupInfo
import com.example.musicapp.domain.repository.GroupRepository

class GroupRepositoryFirebase(
    private val getGroupAllImpl: GetGroupAllImpl,
    private val getGroupWithFilterOnGenresImpl: GetGroupWithFilterOnGenresImpl,
    private val getGroupByIdImpl: GetGroupByIdImpl,
    private val getGroupInfoImpl: GetGroupInfoImpl
): GroupRepository {
    override suspend fun getGroupWithFilterOnGenre(filter: List<Int>): List<Group> {
        return getGroupWithFilterOnGenresImpl.execute(filter)
    }

    override suspend fun getGroupAll(): List<Group> {
        return getGroupAllImpl.execute()
    }

    override suspend fun getGroupById(id: String): Group? {
        return getGroupByIdImpl.execute(id)
    }

    override suspend fun getGroupInfo(groupId: String): GroupInfo? {
        return getGroupInfoImpl.execute(groupId)
    }
}