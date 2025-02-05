package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMusicsFromPlaylistSQLite(private val playlistRepository: PlaylistRepository) {
    fun getMusicsFromPlaylist(playlistId: Long): Flow<List<MusicResult>> {
        if (playlistId <= 0L) {
            return flow { listOf<MusicResult>() }
        }

        return playlistRepository.getMusicsPlaylist(playlistId)
    }

    fun getMusicsFromPlaylist(playlistId: Long, limit: Int): Flow<List<MusicResult>> {
        if (playlistId <= 0L || limit <= 0) {
            return flow { listOf<MusicResult>() }
        }

        return playlistRepository.getMusicsPlaylist(playlistId, limit)
    }
}