package com.example.musicapp.domain.usecase.getAnother

import com.example.musicapp.domain.module.GroupInfo
import com.example.musicapp.domain.repository.GroupRepository

class GetGroupInfo(
    private val groupRepository: GroupRepository
) {
    suspend fun execute(groupId: String): GroupInfo? {
        if (groupId.isEmpty()) {
            return null
        }

        return groupRepository.getGroupInfo(groupId)
    }
}