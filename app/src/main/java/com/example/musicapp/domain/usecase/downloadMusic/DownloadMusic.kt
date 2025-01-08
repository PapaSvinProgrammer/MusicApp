package com.example.musicapp.domain.usecase.downloadMusic

import com.example.musicapp.domain.repository.DownloadMusicRepository

class DownloadMusic(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun execute(musicId: String, url: String) {
        if (musicId.isEmpty() || url.isEmpty()) {
            return
        }

        downloadMusicRepository.download(
            musicId = musicId,
            url = url
        )
    }
}