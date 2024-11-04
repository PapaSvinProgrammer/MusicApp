package com.example.musicapp.domain.usecase.getMusic

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.GetMusicDataRepository

class GetMusicAll(private val getMusicDataRepository: GetMusicDataRepository) {
    fun execute(): LiveData<ArrayList<Music>> {
        return getMusicDataRepository.getMusicAll()
    }
}