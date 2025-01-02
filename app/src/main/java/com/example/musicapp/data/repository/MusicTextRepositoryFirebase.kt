package com.example.musicapp.data.repository

import com.example.musicapp.data.firebase.getAnother.GetMusicTextById
import com.example.musicapp.domain.module.MusicText
import com.example.musicapp.domain.repository.MusicTextRepository

class MusicTextRepositoryFirebase(
    private val getMusicTextById: GetMusicTextById
): MusicTextRepository {
    override suspend fun getTextById(musicId: String): MusicText? {
        return getMusicTextById.execute(musicId)
    }
}