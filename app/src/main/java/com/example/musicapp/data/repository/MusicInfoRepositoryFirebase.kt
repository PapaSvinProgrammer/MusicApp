package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.getAnother.GetMusicInfoById
import com.example.musicapp.domain.module.MusicInfo
import com.example.musicapp.domain.repository.MusicInfoRepository

class MusicInfoRepositoryFirebase(
    private val getMusicInfoById: GetMusicInfoById
): MusicInfoRepository {
    override suspend fun getInfoById(musicId: String): MusicInfo? {
        return getMusicInfoById.execute(musicId)
    }
}