package com.example.musicapp.domain.usecase.getGroup

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository


class GetGroupAll(private val groupRepository: GroupRepository) {
    fun execute(): LiveData<ArrayList<Group>> {
        return groupRepository.getGroupAll()
    }
}