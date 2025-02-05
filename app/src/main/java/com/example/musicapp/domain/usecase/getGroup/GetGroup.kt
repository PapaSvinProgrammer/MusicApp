package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository

class GetGroup(private val groupRepository: GroupRepository) {
    suspend fun getGroupAll(): List<Group> {
        return groupRepository.getGroupAll()
    }

    suspend fun getGroupById(id: String): Group? {
        if (id.isEmpty()) {
            return null
        }

        return groupRepository.getGroupById(id)
    }
}