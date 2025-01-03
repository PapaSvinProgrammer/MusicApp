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

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(music: MusicEntity)

    @Transaction
    @Query("SELECT * FROM favorite_music WHERE firebase_id = :firebaseId")
    fun getMusicById(firebaseId: String): MusicResult?

    @Transaction
    @Query("SELECT * FROM favorite_music ORDER BY id DESC")
    fun getAll(): List<MusicResult?>

    @Transaction
    @Query("SELECT * FROM author_for_music ORDER BY id DESC")
    fun getAllAuthor(): List<AuthorEntity?>

    @Transaction
    @Query("SELECT * FROM favorite_music ORDER BY id DESC LIMIT :limit")
    fun getMusicLimit(limit: String): List<MusicResult?>

    @Transaction
    @Query("SELECT * FROM author_for_music ORDER BY id DESC LIMIT :limit")
    fun getAuthorLimit(limit: String): List<AuthorEntity?>

    @Query("DELETE FROM favorite_music WHERE firebase_id = :firebaseId")
    suspend fun deleteMusicById(firebaseId: String)
}