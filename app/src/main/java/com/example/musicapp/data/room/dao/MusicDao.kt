package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(music: MusicEntity)

    @Transaction
    @Query("SELECT * FROM music WHERE firebase_id = :firebaseId")
    fun getMusicById(firebaseId: String): MusicResult?

    @Transaction
    @Query("SELECT * FROM music " +
            "WHERE firebase_id = :musicFirebaseId AND playlist_id = :playlistId")
    fun getMusicByIdFromPlaylist(musicFirebaseId: String, playlistId: Long): MusicResult?

    @Transaction
    @Query("SELECT * FROM music ORDER BY id DESC")
    fun getAll(): List<MusicResult>

    @Transaction
    @Query("SELECT * FROM music WHERE playlist_id = :playlistId ORDER BY id DESC")
    fun getAllFromPlaylist(playlistId: Long): Flow<List<MusicResult>>

    @Transaction
    @Query("SELECT * FROM music WHERE playlist_id = :playlistId ORDER BY id DESC LIMIT :limit")
    fun getAllFromPlaylist(playlistId: Long, limit: Int): List<MusicResult>

    @Transaction
    @Query("SELECT * FROM author_for_music ORDER BY id DESC")
    fun getAllAuthor(): List<AuthorEntity>

    @Transaction
    @Query("SELECT * FROM music ORDER BY id DESC LIMIT :limit")
    fun getMusicLimit(limit: Int): List<MusicResult>

    @Transaction
    @Query("SELECT * FROM author_for_music ORDER BY id DESC LIMIT :limit")
    fun getAuthorLimit(limit: Int): List<AuthorEntity>

    @Transaction
    @Query("SELECT * FROM music " +
            "JOIN author_for_music ON author_id = author_for_music.firebase_id " +
            "JOIN album_for_music ON album_id = album_for_music.firebase_id " +
            "WHERE name LIKE :searchString " +
            "OR author_name LIKE :searchString " +
            "OR album_name LIKE :searchString")
    fun searchMusic(searchString: String): List<MusicResult>

    @Transaction
    @Query("SELECT * FROM author_for_music WHERE author_name LIKE :searchString")
    fun searchAuthor(searchString: String): List<AuthorEntity>

    @Query("DELETE FROM music WHERE firebase_id = :firebaseId")
    suspend fun deleteMusicById(firebaseId: String)

    @Query("SELECT COUNT(*) FROM music WHERE playlist_id = :playlistId")
    suspend fun getCount(playlistId: Long): Int

    @Query(value = "SELECT SUM(music_time) FROM music WHERE playlist_id = :playlistId")
    suspend fun getTime(playlistId: Long): Long
}