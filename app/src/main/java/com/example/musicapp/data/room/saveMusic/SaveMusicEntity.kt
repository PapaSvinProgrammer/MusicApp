package com.example.musicapp.data.room.saveMusic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "save_music",
    indices = [
        Index(
            value = ["music_id"],
            unique = true
        )
    ]
)
data class SaveMusicEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo("music_id")
    val musicId: String?
)