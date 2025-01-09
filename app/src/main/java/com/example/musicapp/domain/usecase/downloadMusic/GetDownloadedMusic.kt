package com.example.musicapp.domain.usecase.downloadMusic

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
            return getDownloads()
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