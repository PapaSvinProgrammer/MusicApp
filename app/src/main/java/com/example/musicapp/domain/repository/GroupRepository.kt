package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.GroupInfo

interface GroupRepository {
    suspend fun getGroupWithFilterOnGenre(filter: List<Int>): List<Group>
    suspend fun getGroupAll(): List<Group>
    suspend fun getGroupById(id: String): Group?
    suspend fun getGroupInfo(groupId: String): GroupInfo?
}