package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity
import com.example.musicapp.data.room.favoriteMusicEntity.MusicResult

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMusic(music: FavoriteMusicEntity)

    @Transaction
    @Query("SELECT * FROM favorite_music WHERE firebase_id = :firebaseId")
    fun getMusicById(firebaseId: String): MusicResult?

    @Transaction
    @Query("SELECT * FROM favorite_music")
    fun getAll(): List<MusicResult?>

    @Transaction
    @Query("SELECT * FROM author_for_music")
    fun getAllAuthor(): List<AuthorEntity>

    @Query("DELETE FROM favorite_music WHERE firebase_id = :firebaseId")
    suspend fun deleteMusicById(firebaseId: String)
}