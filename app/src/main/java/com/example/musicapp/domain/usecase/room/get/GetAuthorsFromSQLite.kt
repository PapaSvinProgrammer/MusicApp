package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.repository.MusicLiteRepository

class GetAuthorsFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(): List<AuthorEntity> {
        return musicLiteRepository.getAllAuthor()
    }

    suspend fun execute(limit: Int): List<AuthorEntity> {
        if (limit <= 0) {
            return listOf()
        }

        return musicLiteRepository.getAuthorLimit(limit)
    }
}
