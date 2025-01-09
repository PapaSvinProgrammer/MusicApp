package com.example.musicapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.dao.SaveMusicDao
import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.data.room.saveMusic.SaveMusicEntity

@Database(
    version = 1,
    entities = [
        MusicEntity::class,
        AuthorEntity::class,
        AlbumEntity::class,
        PlaylistEntity::class,
        SaveMusicEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getMusicDao(): MusicDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getSaveMusicDao(): SaveMusicDao
}