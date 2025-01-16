package com.example.musicapp.data.repository

import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.firebase.search.SearchAlbumImpl
import com.example.musicapp.data.firebase.search.SearchAllImpl
import com.example.musicapp.data.firebase.search.SearchGroupImpl
import com.example.musicapp.data.firebase.search.SearchMusicImpl
import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.SearchRepository

private const val DEFAULT_SEARCH_LIMIT = 20L

class SearchRepositoryFirebase(
    private val searchMusicImpl: SearchMusicImpl,
    private val searchAlbumImpl: SearchAlbumImpl,
    private val searchGroupImpl: SearchGroupImpl,
    private val searchAllImpl: SearchAllImpl
): SearchRepository {
    override suspend fun searchAll(searchData: SearchData): List<Music> {
        return searchAllImpl.execute(
            searchData = searchData,
            limit = DEFAULT_SEARCH_LIMIT
        )
    }

    override suspend fun searchName(searchData: SearchData): List<Music> {
        return searchMusicImpl.execute(
            searchData = searchData,
            limit = DEFAULT_SEARCH_LIMIT,
            field = DocumentConst.MUSIC_NAME_FIELD
        )
    }

    override suspend fun searchAlbum(searchData: SearchData): List<Album> {
        return searchAlbumImpl.execute(
            searchData = searchData,
            limit = DEFAULT_SEARCH_LIMIT
        )
    }

    override suspend fun searchGroup(searchData: SearchData): List<Group> {
        return searchGroupImpl.execute(
            searchData = searchData,
            limit = DEFAULT_SEARCH_LIMIT
        )
    }
}