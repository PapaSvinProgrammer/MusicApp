package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class SearchMusicLocal(
    private val musicLiteRepository: MusicLiteRepository
) {
    suspend fun execute(text: String): List<MusicResult> {
        if (text.length < 2) {
            return listOf()
        }

        return musicLiteRepository.searchMusic(text)
    }
}