package com.example.musicapp.domain.usecase.room.get

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository

class GetDownloadedMusic(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun getDownloads(): List<Music> {
        return downloadMusicRepository.getDownloads()
    }

    fun getDownloadsLimit(limit: Int): List<Music> {
        if (limit <= 0) {
            return listOf()
        }

        return downloadMusicRepository.getDownloadsLimit(limit)
    }

    fun getDownload(musicId: String): Music? {
        if (musicId.isEmpty()) {
            return null
        }

        return downloadMusicRepository.getDownload(musicId)
    }
}