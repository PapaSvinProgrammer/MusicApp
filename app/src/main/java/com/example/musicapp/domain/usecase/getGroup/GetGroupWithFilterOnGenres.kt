package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.data.constant.GenresConst
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository

class GetGroupWithFilterOnGenres(private val groupRepository: GroupRepository) {
    suspend fun execute(filter: List<Int>): List<Group> {
        return groupRepository.getGroupWithFilterOnGenre(
            filter = convertFilter(filter)
        )
    }

    private fun convertFilter(filter: List<Int>): List<String> {
        val result = ArrayList<String>()

        filter.forEach {
            if (it != 0) {
                result.add(GenresConst.array[it - 1])
            }
        }

        return result
    }
}