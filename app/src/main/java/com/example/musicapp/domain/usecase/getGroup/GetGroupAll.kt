package com.example.musicapp.domain.usecase.getGroup

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GetMusicDataRepository

class GetGroupAll(private val getMusicDataRepository: GetMusicDataRepository) {
    fun execute(): LiveData<ArrayList<Group>> {
        return getMusicDataRepository.getGroupAll()
    }
}