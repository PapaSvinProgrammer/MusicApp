package com.example.musicapp.data.room.musicEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "music"
)
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "firebase_id")
    val firebaseId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,

    @ColumnInfo(name = "album_id")
    val albumId: String,

    @ColumnInfo(name = "author_id")
    val authorId: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "movie_url")
    val movieUrl: String,

    @ColumnInfo(name = "music_time")
    val time: Long
)