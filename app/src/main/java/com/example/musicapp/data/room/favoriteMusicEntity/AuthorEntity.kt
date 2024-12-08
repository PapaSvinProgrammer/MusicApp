package com.example.musicapp.data.room.favoriteMusicEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "author_for_music",
    indices = [
        Index(
            value = ["firebase_id"],
            unique = true
        )
    ]
)
data class AuthorEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "firebase_id")
    val firebaseId: String,

    @ColumnInfo(name = "author_name")
    val name: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)