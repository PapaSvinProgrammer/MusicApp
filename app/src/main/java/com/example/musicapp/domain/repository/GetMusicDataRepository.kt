package com.example.musicapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music

interface GetMusicDataRepository {
    fun getMusicWithFilterOnGroup(): LiveData<ArrayList<Music>>
    fun getMusicWithFilterOnGenre(): LiveData<ArrayList<Music>>
    fun getMusicWithFilterOnAlbum(): LiveData<ArrayList<Music>>
    fun getMusicWithFilterOnName(): LiveData<ArrayList<Music>>
    fun getMusicAll(): LiveData<ArrayList<Music>>

    fun getGroupWithFilterOnName(): LiveData<ArrayList<Group>>
    fun getGroupWithFilterOnGenre(): LiveData<ArrayList<Group>>
    fun getGroupAll(): LiveData<ArrayList<Group>>

    fun getAlbumWithFilterOnGenre(): LiveData<ArrayList<Album>>
    fun getAlbumAll(): LiveData<ArrayList<Album>>
}