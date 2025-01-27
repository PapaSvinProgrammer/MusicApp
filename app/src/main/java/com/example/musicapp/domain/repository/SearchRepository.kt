package com.example.musicapp.domain.repository

import com.example.musicapp.data.module.SearchData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music

interface SearchRepository {
    suspend fun searchAll(searchData: SearchData): List<Music>
    suspend fun searchMusic(searchData: SearchData): List<Music>
    suspend fun searchAlbum(searchData: SearchData): List<Album>
    suspend fun searchGroup(searchData: SearchData): List<Group>
}