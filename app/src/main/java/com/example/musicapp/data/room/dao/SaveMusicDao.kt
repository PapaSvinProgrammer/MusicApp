package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaveMusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(saveMusic: SaveMusicEntity)

    @Query("DELETE FROM save_musics WHERE firebase_id = :firebaseId")
    suspend fun delete(firebaseId: String)

    @Transaction
    @Query("SELECT * FROM save_musics")
    fun getAll(): Flow<List<SaveMusicEntity?>>
}