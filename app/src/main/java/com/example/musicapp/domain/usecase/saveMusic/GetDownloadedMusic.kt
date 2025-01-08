package com.example.musicapp.domain.usecase.saveMusic

import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.repository.DownloadMusicRepository

class GetDownloadedMusic(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun getDownloads(): List<SaveMusic> {
        return downloadMusicRepository.getDownloads()
    }

    fun getDownload(musicId: String): SaveMusic? {
        if (musicId.isEmpty()) {
            return null
        }

        return downloadMusicRepository.getDownload(musicId)
    }
}