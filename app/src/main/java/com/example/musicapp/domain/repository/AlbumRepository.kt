package com.example.musicapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Album

interface AlbumRepository {
    fun getAlbumWithFilterOnGenre(): LiveData<ArrayList<Album>>
    fun getAlbumAll(): LiveData<ArrayList<Album>>
}