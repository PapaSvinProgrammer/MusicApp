package com.example.musicapp.domain.usecase.search

import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.SearchRepository

class SearchAlbum(
    private val searchRepository: SearchRepository
) {
    suspend fun execute(text: String): List<Album> {
        if (text.length < 2) {
            return listOf()
        }

        return searchRepository.searchAlbum(
            searchData = convertData(text)
        )
    }

    private fun convertData(text: String): SearchData {
        return SearchData(
            text = text
        )
    }
}