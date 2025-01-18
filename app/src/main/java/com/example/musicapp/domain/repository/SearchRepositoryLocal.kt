package com.example.musicapp.domain.repository

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.module.Music

interface SearchRepositoryLocal {
    suspend fun searchMusic(text: String): List<MusicResult>
    suspend fun searchPlaylist(text: String): List<PlaylistResult>
    suspend fun searchDownloaded(text: String): List<Music>
}