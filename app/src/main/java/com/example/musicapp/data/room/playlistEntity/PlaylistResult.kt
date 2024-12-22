package com.example.musicapp.data.room.playlistEntity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult

data class PlaylistResult (
    @Embedded
    val playlistEntity: PlaylistEntity,

    @Relation(
        entity = MusicEntity::class,
        parentColumn = "id",
        entityColumn = "playlist_id"
    )
    val musicResult: List<MusicResult?>
)