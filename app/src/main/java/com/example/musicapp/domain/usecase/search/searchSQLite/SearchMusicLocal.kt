package com.example.musicapp.domain.usecase.search.searchSQLite

import com.example.musicapp.data.room.musicEntity.MusicResult

class SearchMusicLocal {
    fun execute(text: String): List<MusicResult?> {
        if (text.length < 2) {
            return listOf()
        }

        return listOf()
    }
}