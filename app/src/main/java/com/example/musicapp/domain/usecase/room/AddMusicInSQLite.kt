package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository

class AddMusicInSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(music: Music, playlistId: Long) {
        musicLiteRepository.add(
            albumEntity = convertAlbum(music),
            authorEntity = convertAuthor(music),
            musicEntity = convertMusic(music, playlistId)
        )
    }

    private fun convertAlbum(music: Music): AlbumEntity {
        return AlbumEntity(
            firebaseId = music.album,
            name = "album_name",
            imageLow = music.imageLow,
            imageHigh = music.imageHigh
        )
    }

    private fun convertAuthor(music: Music): AuthorEntity {
        return AuthorEntity(
            id = 0,
            firebaseId = music.groupId,
            name = music.group,
            imageUrl = music.imageGroup
        )
    }

    private fun convertMusic(music: Music, playlistId: Long): MusicEntity {
        return MusicEntity(
            id = 0,
            firebaseId = music.id,
            name = music.name,
            playlistId = playlistId,
            albumId = music.album,
            authorId = music.groupId,
            url = music.url,
            saveUri = "",
        )
    }
}