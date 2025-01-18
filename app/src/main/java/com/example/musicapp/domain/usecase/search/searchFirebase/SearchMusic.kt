package com.example.musicapp.domain.usecase.search.searchFirebase

import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.SearchRepository

class SearchMusic(
    private val searchRepository: SearchRepository
) {
    suspend fun execute(text: String): List<Music> {
        if (text.length < 2) {
            return listOf()
        }

        return searchRepository.searchName(
            searchData = convertData(text)
        )
    }

    private fun convertData(text: String): SearchData {
        return SearchData(
            text = text
        )
    }
}