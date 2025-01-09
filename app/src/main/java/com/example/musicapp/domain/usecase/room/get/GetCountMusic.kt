package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.repository.MusicLiteRepository

class GetCountMusic(
    private val musicLiteRepository: MusicLiteRepository
) {
    suspend fun execute(): Int {
        return musicLiteRepository.getCount()
    }
}