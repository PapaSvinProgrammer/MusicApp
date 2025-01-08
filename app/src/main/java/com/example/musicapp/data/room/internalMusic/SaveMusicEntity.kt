package com.example.musicapp.data.room.internalMusic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "save_musics",
    indices = [
        Index(
            value = ["firebase_id"],
            unique = true
        )
    ]
)
data class SaveMusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "firebase_id")
    val firebaseId: String? = null
)