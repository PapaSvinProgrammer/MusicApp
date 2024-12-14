package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistResult
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlistEntity: PlaylistEntity)

    //TODO
    @Transaction
    @Query("SELECT * FROM playlists ORDER BY :filter")
    fun getAll(filter: String): Flow<List<PlaylistResult?>>

    //TODO
    @Transaction
    @Query("SELECT * FROM playlists ORDER BY :filter")
    fun getAllOnlyPlaylist(filter: String): Flow<List<PlaylistEntity?>>

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun delete(id: String)
}