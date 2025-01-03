package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository

class AddMusicInSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(music: Music, playlistId: Long = 1) {
        musicLiteRepository.add(
            MusicResult(
                musicEntity = convertMusic(music, playlistId),
                albumEntity = convertAlbum(music),
                authorEntity = convertAuthor(music)
            )
        )
    }

    private fun convertAlbum(music: Music): AlbumEntity {
        return AlbumEntity(
            firebaseId = music.albumId.toString(),
            name = "album_name",
            imageLow = music.imageLow.toString(),
            imageHigh = music.imageHigh.toString()
        )
    }

    private fun convertAuthor(music: Music): AuthorEntity {
        return AuthorEntity(
            id = 0,
            firebaseId = music.groupId.toString(),
            name = music.group.toString(),
            imageUrl = music.imageGroup.toString()
        )
    }

    private fun convertMusic(music: Music, playlistId: Long): MusicEntity {
        return MusicEntity(
            id = 0,
            firebaseId = music.id.toString(),
            name = music.name.toString(),
            playlistId = playlistId,
            albumId = music.albumId.toString(),
            authorId = music.groupId.toString(),
            url = music.url.toString(),
            saveUri = "",
            movieUrl = music.movieUrl ?: "",
            time = music.time.toString()
        )
    }
}