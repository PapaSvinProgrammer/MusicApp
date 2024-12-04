package com.example.musicapp.data.room.favoriteMusicEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "album_for_music")
data class AlbumEntity (
    @PrimaryKey
    @ColumnInfo(name = "firebase_id")
    val firebaseId: String,

    @ColumnInfo(name = "album_name")
    val name: String,

    @ColumnInfo(name = "image_low")
    val imageLow: String,

    @ColumnInfo(name = "image_high")
    val imageHigh: String
)