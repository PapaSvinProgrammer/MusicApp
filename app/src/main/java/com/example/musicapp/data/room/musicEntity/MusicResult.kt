package com.example.musicapp.data.room.musicEntity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.musicapp.data.room.internalMusic.SaveMusicEntity

data class MusicResult(
    @Embedded
    val musicEntity: MusicEntity,

    @Relation(
        entity = AuthorEntity::class,
        parentColumn = "author_id",
        entityColumn = "firebase_id"
    )
    val authorEntity: AuthorEntity,

    @Relation(
        entity = AlbumEntity::class,
        parentColumn = "album_id",
        entityColumn = "firebase_id"
    )
    val albumEntity: AlbumEntity,

    @Relation(
        entity = SaveMusicEntity::class,
        parentColumn = "firebase_id",
        entityColumn = "firebase_id"
    )
    val saveMusicEntity: SaveMusicEntity
)