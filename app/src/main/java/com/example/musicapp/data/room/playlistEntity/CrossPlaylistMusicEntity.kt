package com.example.musicapp.data.room.playlistEntity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "cross_playlist_and_music",
    primaryKeys = ["music_id", "playlist_id"]
)
data class CrossPlaylistMusicEntity(
    @ColumnInfo(name = "music_id")
    val musicId: String,

    @ColumnInfo(name = "playlist_id")
    val playlistId: Long
)