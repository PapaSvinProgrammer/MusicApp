package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.presintation.bottomSheet.FilterBottomSheet
import kotlinx.coroutines.flow.Flow

private const val NAME_FILTER = "name_playlist"
private const val ID_FILTER = "id"
private const val DATE_FILTER = "date"

class GetPlaylistFromSQLite(private val playlistRepository: PlaylistRepository) {
    fun executeToAll(filter: Int = 0): Flow<List<PlaylistResult?>> {
        return playlistRepository.getAll(
            filter = convertFilterToString(filter)
        )
    }

    fun executeToLimit(filter: Int = 0): Flow<List<PlaylistEntity?>> {
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