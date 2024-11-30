package com.example.musicapp.domain.usecase.getMusic

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class GetMusicAll(private val musicRepository: MusicRepository) {
    fun execute(): LiveData<ArrayList<Music>> {
        return musicRepository.getMusicAll()
    }
}