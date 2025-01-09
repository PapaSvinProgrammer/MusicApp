package com.example.musicapp.domain.usecase.downloadMusic

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository

class DownloadMusic(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun execute(music: Music) {
        if (music.id.isNullOrEmpty()) {
            return
        }

        downloadMusicRepository.download(music)
    }
}