package com.example.musicapp.app.support

import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.data.room.playlistEntity.CrossPlaylistMusicEntity
import com.example.musicapp.domain.module.Music

class ConvertMusic {
    companion object {
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

        fun convertItemToMusic(it: MusicResult?): Music {
            return Music(
                id = it?.musicEntity?.firebaseId,
                albumId = it?.albumEntity?.firebaseId,
                albumName = it?.albumEntity?.name,
                groupId = it?.authorEntity?.firebaseId,
                group = it?.authorEntity?.name,
                imageGroup = it?.authorEntity?.imageUrl,
                imageLow = it?.albumEntity?.imageLow,
                imageHigh = it?.albumEntity?.imageHigh,
                movieUrl = it?.musicEntity?.movieUrl,
                name = it?.musicEntity?.name,
                time = it?.musicEntity?.time ?: 0,
                url = it?.musicEntity?.url
            )
        }

        fun convertListToMusic(list: List<MusicResult>): List<Music> {
            return list.map {
                Music(
                    id = it.musicEntity.firebaseId,
                    albumId = it.albumEntity.firebaseId,
                    albumName = it.albumEntity.name,
                    groupId = it.authorEntity.firebaseId,
                    group = it.authorEntity.name,
                    imageGroup = it.authorEntity.imageUrl,
                    imageLow = it.albumEntity.imageLow,
                    imageHigh = it.albumEntity.imageHigh,
                    movieUrl = it.musicEntity.movieUrl,
                    name = it.musicEntity.name,
                    time = it.musicEntity.time,
                    url = it.musicEntity.url
                )
            }.toList()
        }
    }
}