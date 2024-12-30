package com.example.musicapp.domain.usecase.getMusicText

import com.example.musicapp.domain.module.MusicText
import com.example.musicapp.domain.repository.MusicTextRepository

class GetMusicText(private val musicTextRepository: MusicTextRepository) {
    suspend fun getTextById(musicId: String): MusicText? {
        return convertMusicTExtList(
            list = musicTextRepository.getTextById(musicId)
        )
    }

    private fun convertMusicTExtList(list: List<MusicText>): MusicText? {
        if (list.isEmpty()) {
            return null
        }
        else {
            return list.first()
        }
    }
}