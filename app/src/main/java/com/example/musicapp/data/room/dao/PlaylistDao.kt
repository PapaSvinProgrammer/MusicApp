package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(playlistEntity: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY id")
    fun getAllById(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY name_playlist")
    fun getAllByName(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY date")
    fun getAllByDate(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY id DESC LIMIT :limit")
    fun getOnlyPlaylistLimit(limit: Int): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY id DESC")
    fun getOnlyPlaylist(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query("SELECT * FROM music " +
            "JOIN cross_playlist_and_music ON cross_playlist_and_music.playlist_id = :playlistId " +
            "AND music.firebase_id =  cross_playlist_and_music.music_id " +
            "ORDER BY id DESC")
    fun getMusicsPlaylist(playlistId: Long): Flow<List<MusicResult>>

    @Query("SELECT * FROM music " +
            "JOIN cross_playlist_and_music ON cross_playlist_and_music.playlist_id = :playlistId " +
            "AND music.firebase_id =  cross_playlist_and_music.music_id " +
            "ORDER BY id DESC LIMIT :limit")
    fun getMusicsPlaylist(playlistId: Long, limit: Int): Flow<List<MusicResult>>

    @Transaction
    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun delete(id: String)

    @Transaction
    @Query("UPDATE playlists SET image_url = :url WHERE id = :id")
    suspend fun saveImage(url: String, id: String)

    @Transaction
    @Query("UPDATE playlists SET name_playlist = :name WHERE id = :id")
    suspend fun saveName(name: String, id: String)

    @Query("SELECT * FROM playlists WHERE name_playlist LIKE :searchString")
    suspend fun search(searchString: String): List<PlaylistEntity>

    @Query("SELECT COUNT(*) FROM playlists")
    fun getCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM cross_playlist_and_music WHERE playlist_id = :playlistId")
    fun getCountMusicInPlaylist(playlistId: Long): Flow<Int>
}