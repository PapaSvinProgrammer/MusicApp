package com.example.musicapp.domain.usecase.search

import com.example.musicapp.domain.module.Music

class SearchAll {
    fun execute(text: String, list: List<Music>): List<Music>? {
        if (text.length < 2) {
            return null
        }

        if (list.isEmpty()) {
            return null
        }

        return list.filter { item ->
            (item.name?.trim()?.lowercase()
                    + item.group?.trim()?.lowercase()
                    + item.albumName?.trim()?.lowercase()
                    )
                .contains(text.trim().lowercase())
        }.toList()
    }
}