package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository


class GetGroupAll(private val groupRepository: GroupRepository) {
    suspend fun execute(): List<Group> {
        return groupRepository.getGroupAll()
    }
}