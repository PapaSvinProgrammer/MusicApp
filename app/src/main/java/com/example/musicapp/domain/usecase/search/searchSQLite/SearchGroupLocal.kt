package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.repository.MusicLiteRepository

class SearchGroupLocal(
    private val musicLiteRepository: MusicLiteRepository
) {
    suspend fun execute(text: String): List<AuthorEntity> {
        if (text.length < 2) {
            return listOf()
        }

        return musicLiteRepository.searchAuthor(text)
    }
}