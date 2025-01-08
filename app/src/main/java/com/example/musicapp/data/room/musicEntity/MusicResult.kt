package com.example.musicapp.data.room.musicEntity

import androidx.room.Embedded
import androidx.room.Relation

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
    val albumEntity: AlbumEntity
)