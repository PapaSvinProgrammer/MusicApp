package com.example.musicapp.data.repository

import com.example.musicapp.data.constant.DocumentConst
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicsByFieldIdImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicsFilterImpl
import com.example.musicapp.data.firebase.getMusic.GetRandomMusicImpl
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository
import com.google.firebase.firestore.Query

class MusicRepositoryFirebase(
    private val getMusicAllImpl: GetMusicAllImpl,
    private val getMusicsByFieldIdImpl: GetMusicsByFieldIdImpl,
    private val getRandomMusicImpl: GetRandomMusicImpl,
    private val getMusicsFilterImpl: GetMusicsFilterImpl
): MusicRepository {
    override suspend fun getRandomMusic(limit: Long): List<Music> {
        return getRandomMusicImpl.execute(limit)
    }

    override suspend fun getMusicAll(): List<Music> {
        return getMusicAllImpl.execute()
    }

    override suspend fun getMusicsByAlbumId(albumId: String): List<Music> {
        return getMusicsByFieldIdImpl.execute(
            anyId = albumId,
            field = DocumentConst.MUSIC_ALBUM_ID_FIELD
        )
    }

    override suspend fun getMusicByAuthorIdOrderRating(authorId: String): List<Music> {
        //TODO
        return getMusicsFilterImpl.execute(
            id = authorId,
            field = DocumentConst.MUSIC_GROUP_ID_FIELD,
            filter = DocumentConst.MUSIC_NAME_FIELD,
            sort = Query.Direction.DESCENDING
        )
    }

    override suspend fun getMusicByAuthorIdOrderName(authorId: String): List<Music> {
       return getMusicsFilterImpl.execute(
           id = authorId,
           field = DocumentConst.MUSIC_GROUP_ID_FIELD,
           filter = DocumentConst.MUSIC_NAME_FIELD
       )
    }

    override suspend fun getMusicByAuthorIdOrderAlbum(authorId: String): List<Music> {
        return getMusicsFilterImpl.execute(
            id = authorId,
            field = DocumentConst.MUSIC_GROUP_ID_FIELD,
            filter = DocumentConst.MUSIC_ALBUM_NAME_FIELD
        )
    }
}