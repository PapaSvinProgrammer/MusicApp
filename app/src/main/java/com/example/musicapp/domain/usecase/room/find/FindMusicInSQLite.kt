package com.example.musicapp.domain.usecase.room.find

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.MusicLiteRepository

class FindMusicInSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun find(id: String): MusicResult? {
        if (id.isEmpty()) {
            return null
        }

        return musicLiteRepository.findMusicById(id)
    }

    suspend fun findInPlaylist(musicId: String, playlistId: Long): MusicResult? {
        if (musicId.isEmpty() || playlistId <= 0L) {
            return null
        }

        return musicLiteRepository.findMusicByIdFromPlaylist(
            musicFirebaseId = musicId,
            playlistId = playlistId
        )
    }
}