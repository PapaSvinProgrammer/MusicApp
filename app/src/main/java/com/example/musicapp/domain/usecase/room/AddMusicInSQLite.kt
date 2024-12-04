package com.example.musicapp.domain.usecase.room

import com.example.musicapp.data.room.favoriteMusicEntity.AlbumEntity
import com.example.musicapp.data.room.favoriteMusicEntity.AuthorEntity
import com.example.musicapp.data.room.favoriteMusicEntity.FavoriteMusicEntity
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository

class AddMusicInSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(music: Music) {
        musicLiteRepository.add(
            albumEntity = convertAlbum(music),
            authorEntity = convertAuthor(music),
            favoriteMusicEntity = convertMusic(music)
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
            firebaseId = music.groupId,
            name = music.group
        )
    }

    private fun convertMusic(music: Music): FavoriteMusicEntity {
        return FavoriteMusicEntity(
            firebaseId = music.id,
            name = music.name,
            albumId = music.album,
            authorId = music.groupId,
            url = music.url,
            saveUri = ""
        )
    }
}