package com.example.musicapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity

@Database(
    version = 1,
    entities = [
        FavoriteMusicEntity::class,
        AuthorEntity::class,
        AlbumEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getMusicDao(): MusicDao
}