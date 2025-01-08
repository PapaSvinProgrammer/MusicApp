package com.example.musicapp.domain.usecase.saveMusic

import com.example.musicapp.domain.repository.DownloadMusicRepository

class DeleteDownloadMusic(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun execute(musicId: String) {
        if (musicId.isEmpty()) {
            return
        }

        downloadMusicRepository.remove(musicId)
    }
}