package com.example.musicapp.data.room.favoriteMusicEntity

import androidx.room.Embedded
import androidx.room.Relation

data class MusicResult(
    @Embedded
    val favoriteMusicEntity: FavoriteMusicEntity,

    @Relation(
        entity = AuthorEntity::class,
        parentColumn = "author_id",
        entityColumn = "id"
    )
    val authorEntity: AuthorEntity,

    @Relation(
        entity = AlbumEntity::class,
        parentColumn = "album_id",
        entityColumn = "id"
    )
    val albumEntity: AlbumEntity
)