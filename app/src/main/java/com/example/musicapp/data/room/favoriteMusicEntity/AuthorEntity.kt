package com.example.musicapp.data.room.favoriteMusicEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "author_for_music")
data class AuthorEntity (
    @PrimaryKey
    @ColumnInfo(name = "firebase_id")
    val firebaseId: String,

    @ColumnInfo(name = "author_name")
    val name: String
)