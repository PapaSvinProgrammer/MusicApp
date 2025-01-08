package com.example.musicapp.domain.usecase.saveMusic

import com.example.musicapp.domain.repository.DownloadMusicRepository

class ManageDownload(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun stop() {
        downloadMusicRepository.stop()
    }

    fun resume() {
        downloadMusicRepository.resume()
    }
}