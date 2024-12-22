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

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getById(id: String): PlaylistResult?

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY id")
    fun getAllById(): Flow<List<PlaylistResult?>>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY name_playlist")
    fun getAllByName(): Flow<List<PlaylistResult?>>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY date")
    fun getAllByDate(): Flow<List<PlaylistResult?>>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getOnlyPlaylist(): Flow<List<PlaylistEntity?>>

    @Transaction
    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun delete(id: String)

    @Transaction
    @Query("UPDATE playlists SET image_url = :url WHERE id = :id")
    suspend fun saveImage(url: String, id: String)

    @Transaction
    @Query("UPDATE playlists SET name_playlist = :name WHERE id = :id")
    suspend fun saveName(name: String, id: String)
}