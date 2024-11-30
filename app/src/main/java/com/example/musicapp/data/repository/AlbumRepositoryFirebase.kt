package com.example.musicapp.data.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.repository.AlbumRepository

class AlbumRepositoryFirebase: AlbumRepository {
    override fun getAlbumWithFilterOnGenre(): LiveData<ArrayList<Album>> {
        TODO("Not yet implemented")
    }

    override fun getAlbumAll(): LiveData<ArrayList<Album>> {
        TODO("Not yet implemented")
    }
}