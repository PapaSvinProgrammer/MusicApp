package com.example.musicapp.domain.usecase.search.searchFirebase

import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.SearchRepository

class SearchGroup(
    private val searchRepository: SearchRepository
) {
    suspend fun execute(text: String): List<Group> {
        if (text.length < 2) {
            return listOf()
        }

        return searchRepository.searchGroup(convertData(text))
    }

    private fun convertData(text: String): SearchData {
        return SearchData(
            text = text
        )
    }
}