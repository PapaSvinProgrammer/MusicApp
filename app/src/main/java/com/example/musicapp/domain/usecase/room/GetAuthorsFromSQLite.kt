package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.domain.repository.MusicLiteRepository

class GetAuthorsFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(): List<AuthorEntity?> {
        return musicLiteRepository.getAllAuthor()
    }

    suspend fun execute(limit: Int): List<AuthorEntity?> {
        return musicLiteRepository.getAuthorLimit(limit.toString())
    }
}
