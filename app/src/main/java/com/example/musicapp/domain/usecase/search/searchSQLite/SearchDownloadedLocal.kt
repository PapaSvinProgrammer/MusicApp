package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository

class SearchDownloadedLocal(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun execute(text: String): List<Music> {
        if (text.length < 2) {
            return listOf()
        }

        return downloadMusicRepository.search(text)
    }
}