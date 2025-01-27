package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository

class SearchPlaylistLocal(
    private val playlistRepository: PlaylistRepository
) {
    suspend fun execute(text: String): List<PlaylistResult> {
        if (text.length < 2) {
            return listOf()
        }

        return playlistRepository.search(text)
    }
}