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
import com.example.musicapp.data.room.playlistEntity.CrossPlaylistMusicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(music: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossMusicPlaylist(crossPlaylistMusicEntity: CrossPlaylistMusicEntity)

    @Transaction
    @Query("SELECT * FROM music WHERE firebase_id = :firebaseId")
    fun getMusicById(firebaseId: String): MusicResult?

    @Transaction
    @Query("SELECT * FROM music ORDER BY id DESC")
    fun getAll(): Flow<List<MusicResult>>

    @Transaction
    @Query("SELECT * FROM author_for_music ORDER BY id DESC")
    fun getAllAuthor(): Flow<List<AuthorEntity>>

    @Transaction
    @Query("SELECT * FROM music ORDER BY id DESC LIMIT :limit")
    fun getMusicLimit(limit: Int): Flow<List<MusicResult>>

    @Transaction
    @Query("SELECT * FROM author_for_music ORDER BY id DESC LIMIT :limit")
    fun getAuthorLimit(limit: Int): Flow<List<AuthorEntity>>

    @Transaction
    @Query("SELECT * FROM music " +
            "JOIN author_for_music ON author_id = author_for_music.firebase_id " +
            "JOIN album_for_music ON album_id = album_for_music.firebase_id " +
            "WHERE name LIKE :searchString " +
            "OR author_name LIKE :searchString " +
            "OR album_name LIKE :searchString")
    suspend fun searchMusic(searchString: String): List<MusicResult>

    @Transaction
    @Query("SELECT * FROM author_for_music WHERE author_name LIKE :searchString")
    suspend fun searchAuthor(searchString: String): List<AuthorEntity>

    @Query("DELETE FROM music WHERE firebase_id = :firebaseId")
    suspend fun deleteMusicById(firebaseId: String)

    @Query("DELETE FROM cross_playlist_and_music " +
            "WHERE music_id = :musicId AND playlist_id = :playlistId")
    suspend fun deleteFromCrossPlaylistMusic(musicId: String, playlistId: Long)
}