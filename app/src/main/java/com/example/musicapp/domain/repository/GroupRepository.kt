package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Group

interface GroupRepository {
    suspend fun getGroupWithFilterOnName(): List<Group>
    suspend fun getGroupWithFilterOnGenre(filter: List<String>): List<Group>
    suspend fun getGroupAll(): List<Group>
    suspend fun getGroupById(id: String): Group
}