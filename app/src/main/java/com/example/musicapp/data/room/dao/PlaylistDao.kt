package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlistEntity: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY :filter")
    fun getAll(filter: String): List<PlaylistResult?>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY :filter")
    fun getAllOnlyPlaylist(filter: String): List<PlaylistEntity?>

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun delete(id: String)
}