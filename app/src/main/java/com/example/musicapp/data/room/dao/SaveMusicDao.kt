package com.example.musicapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicapp.data.room.saveMusic.SaveMusicEntity

@Dao
interface SaveMusicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: SaveMusicEntity)

    @Query("DELETE FROM save_music WHERE music_id = :musicId")
    suspend fun delete(musicId: String)
}