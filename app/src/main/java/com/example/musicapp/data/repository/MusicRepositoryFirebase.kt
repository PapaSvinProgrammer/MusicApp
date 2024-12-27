package com.example.musicapp.data.repository

import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicsByFieldIdImpl
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class MusicRepositoryFirebase(
    private val getMusicAllImpl: GetMusicAllImpl,
    private val getMusicsByFieldIdImpl: GetMusicsByFieldIdImpl
): MusicRepository {
    override suspend fun getMusicWithFilterOnGroup(): List<Music> {
        TODO("Not yet implemented")
    }

    override suspend fun getMusicWithFilterOnGenre(): List<Music> {
        TODO("Not yet implemented")
    }

    override suspend fun getMusicWithFilterOnAlbum(): List<Music> {
        TODO("Not yet implemented")
    }

    override suspend fun getMusicWithFilterOnName(): List<Music> {
        TODO("Not yet implemented")
    }

    override suspend fun getMusicAll(): List<Music> {
        return getMusicAllImpl.execute()
    }

    override suspend fun getMusicsByAlbumId(albumId: String): List<Music> {
        return getMusicsByFieldIdImpl.execute(
            anyId = albumId,
            field = DocumentConst.MUSIC_ALBUM_FIELD
        )
    }

    override suspend fun getMusicByAuthorId(authorId: String): List<Music> {
        return getMusicsByFieldIdImpl.execute(
            anyId = authorId,
            field = DocumentConst.MUSIC_GROUP_ID_FIELD
        )
    }
}