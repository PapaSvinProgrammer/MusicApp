package com.example.musicapp.domain.usecase.getAnother

import com.example.musicapp.domain.module.MusicText
import com.example.musicapp.domain.repository.MusicTextRepository

class GetMusicText(private val musicTextRepository: MusicTextRepository) {
    suspend fun getTextById(musicId: String): MusicText? {
        return musicTextRepository.getTextById(musicId)
    }
}