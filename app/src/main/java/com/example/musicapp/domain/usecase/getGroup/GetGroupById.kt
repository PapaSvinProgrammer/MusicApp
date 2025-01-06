package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository

class GetGroupById(private val groupRepository: GroupRepository) {
    suspend fun execute(id: String): Group? {
        if (id.isEmpty()) {
            return null
        }

        return groupRepository.getGroupById(id)
    }
}