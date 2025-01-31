package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository

class GetGroupWithFilterOnGenres(private val groupRepository: GroupRepository) {
    suspend fun execute(filter: List<Int>): List<Group> {
        return groupRepository.getGroupWithFilterOnGenre(filter)
    }
}