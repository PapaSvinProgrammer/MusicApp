package com.example.musicapp.domain.usecase.downloadMusic

import com.example.musicapp.domain.repository.DownloadMusicRepository

class GetCountDownloadMusic(
    private val downloadMusicRepository: DownloadMusicRepository
) {
    fun execute(): Int {
        return downloadMusicRepository.getCount()
    }
}