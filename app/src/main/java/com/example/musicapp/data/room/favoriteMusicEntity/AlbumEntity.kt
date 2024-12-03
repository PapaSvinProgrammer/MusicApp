package com.example.musicapp.data.room.favoriteMusicEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album_for_music",
    indices = [
        Index(
            value = ["album_name"],
            unique = true
        )
    ]
)
data class AlbumEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "album_name")
    val name: String,

    @ColumnInfo(name = "image_low")
    val imageLow: String,

    @ColumnInfo(name = "image_high")
    val imageHigh: String,

    @ColumnInfo(name = "firebase_id")
    val firebaseUrl: String
)