package com.example.musicapp.app.support

import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.CrossPlaylistMusicEntity
import com.example.musicapp.domain.module.Music

class ConvertMusic {
    fun convertToMusicResult(music: Music): MusicResult {
        val album = AlbumEntity(
            firebaseId = music.albumId.toString(),
            name = music.albumName.toString(),
            imageLow = music.imageLow.toString(),
            imageHigh = music.imageHigh.toString()
        )

        val author = AuthorEntity(
            id = 0,
            firebaseId = music.groupId.toString(),
            name = music.group.toString(),
            imageUrl = music.imageGroup.toString()
        )

        val musicEntity = MusicEntity(
            id = 0,
            firebaseId = music.id.toString(),
            name = music.name.toString(),
            albumId = music.albumId.toString(),
            authorId = music.groupId.toString(),
            url = music.url.toString(),
            movieUrl = music.movieUrl ?: "",
            time = music.time
        )

        return MusicResult(
            musicEntity = musicEntity,
            albumEntity = album,
            authorEntity = author,
            saveMusicEntity = null
        )
    }

    fun convertToCrossMusicPlaylist(musicId: String, playlistId: Long): CrossPlaylistMusicEntity {
        return CrossPlaylistMusicEntity(
            musicId = musicId,
            playlistId = playlistId
        )
    }
}