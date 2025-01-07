package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository

class AddMusicInSQLite(private val musicLiteRepository: MusicLiteRepository) {
    suspend fun execute(music: Music?, playlistId: Long = 1) {
        if (music == null) {
            return
        }

        musicLiteRepository.add(
            MusicResult(
                musicEntity = convertMusic(music, playlistId),
                albumEntity = convertAlbum(music),
                authorEntity = convertAuthor(music),
                saveMusicEntity = SaveMusicEntity()
            )
        )
    }

    private fun convertAlbum(music: Music): AlbumEntity {
        return AlbumEntity(
            firebaseId = music.albumId.toString(),
            name = music.albumName.toString(),
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
            movieUrl = music.movieUrl ?: "",
            time = music.time.toString()
        )
    }
}