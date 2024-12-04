package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicRepository

class MusicRepositoryFirebase(
    private val getMusicAllImpl: GetMusicAllImpl
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
}