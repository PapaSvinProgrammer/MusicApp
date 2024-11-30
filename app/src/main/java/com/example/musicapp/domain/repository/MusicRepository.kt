package com.example.musicapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.module.Music

interface MusicRepository {
    fun getMusicWithFilterOnGroup(): LiveData<ArrayList<Music>>
    fun getMusicWithFilterOnGenre(): LiveData<ArrayList<Music>>
    fun getMusicWithFilterOnAlbum(): LiveData<ArrayList<Music>>
    fun getMusicWithFilterOnName(): LiveData<ArrayList<Music>>
    fun getMusicAll(): LiveData<ArrayList<Music>>
}