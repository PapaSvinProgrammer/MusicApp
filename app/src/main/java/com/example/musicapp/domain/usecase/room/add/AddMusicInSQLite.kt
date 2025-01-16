package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository
import com.example.musicapp.support.convertAnother.ConvertMusic

class AddMusicInSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(music: Music?, playlistId: Long = 1) {
        if (music == null) {
            return
        }

        musicLiteRepository.add(
            ConvertMusic().convertToMusicResult(music, playlistId)
        )
    }
}