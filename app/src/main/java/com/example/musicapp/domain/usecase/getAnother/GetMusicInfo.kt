package com.example.musicapp.domain.usecase.getAnother

import com.example.musicapp.domain.module.MusicInfo
import com.example.musicapp.domain.repository.MusicInfoRepository

class GetMusicInfo(private val musicInfoRepository: MusicInfoRepository) {
    suspend fun execute(musicId: String): MusicInfo? {
        return musicInfoRepository.getInfoById(musicId)
    }
}