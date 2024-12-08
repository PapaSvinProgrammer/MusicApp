package com.example.musicapp.data.room.favoriteMusicEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "favorite_music",
    indices = [
        Index(
            value = ["firebase_id"],
            unique = true
        )
    ]
)
data class FavoriteMusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "firebase_id")
    val firebaseId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "album_id")
    val albumId: String,

    @ColumnInfo(name = "author_id")
    val authorId: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "save_uri")
    val saveUri: String
)