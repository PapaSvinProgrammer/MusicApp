package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.presintation.bottomSheet.FilterBottomSheet

private const val NAME_FILTER = "name"
private const val ID_FILTER = "id"
private const val DATE_FILTER = "date"

class GetPlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    suspend fun executeToAll(filter: Int = 0): List<PlaylistResult?> {
        return playlistRepository.getAll(
            filter = convertFilterToString(filter)
        )
    }

    suspend fun executeToLimit(filter: Int = 0): List<PlaylistEntity?> {
        return playlistRepository.getOnlyPlaylist(
            filter = convertFilterToString(filter)
        )
    }

    private fun convertFilterToString(filter: Int): String {
        return when (filter) {
            FilterBottomSheet.BY_ALPHABET -> NAME_FILTER
            FilterBottomSheet.BY_DATE_CREATE -> DATE_FILTER
            else -> ID_FILTER
        }
    }
}