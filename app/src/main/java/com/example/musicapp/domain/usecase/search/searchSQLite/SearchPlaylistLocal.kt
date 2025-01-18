package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.playlistEntity.PlaylistResult

class SearchPlaylistLocal {
    fun execute(text: String): List<PlaylistResult?> {
        if (text.length < 2) {
            return listOf()
        }

        return listOf()
    }
}