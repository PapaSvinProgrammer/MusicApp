package com.example.musicapp.domain.repository

interface GetMusicDataRepository {
    fun getMusicWithFilterOnGroup()
    fun getMusicWithFilterOnGenre()
    fun getMusicWithFilterOnAlbum()
    fun getMusicWithFilterOnName()
    fun getMusicAll()

    fun getGroupWithFilterOnName()
    fun getGroupWithFilterOnGenre()
    fun getGroupAll()

    fun getAlbumWithFilterOnGenre()
    fun getAlbumAll()
}