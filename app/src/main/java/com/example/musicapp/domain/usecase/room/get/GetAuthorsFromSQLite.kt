package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAuthorsFromSQLite(private val musicLiteRepository: MusicLiteRepository) {
    fun execute(): Flow<List<AuthorEntity>> {
        return musicLiteRepository.getAllAuthor()
    }

    fun execute(limit: Int): Flow<List<AuthorEntity>> {
        if (limit <= 0) {
            return flow { listOf<AuthorEntity>() }
        }

        return musicLiteRepository.getAuthorLimit(limit)
    }
}
