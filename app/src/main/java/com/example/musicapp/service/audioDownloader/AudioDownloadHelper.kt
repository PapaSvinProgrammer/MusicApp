package com.example.musicapp.service.audioDownloader

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadCursor
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.repository.DownloadMusicRepository

@UnstableApi
class AudioDownloadHelper(
    private val context: Context
): DownloadMusicRepository {

    override fun download(musicId: String, url: String) {
        val downloadRequest = DownloadRequest.Builder(
            musicId,
            url.toUri()
        ).build()

        DownloadService.sendAddDownload(
            context,
            AudioDownloadService::class.java,
            downloadRequest,
            false
        )
    }

    override fun remove(musicId: String) {
        DownloadService.sendRemoveDownload(
            context,
            AudioDownloadService::class.java,
            musicId,
            false
        )
    }

    override fun stop() {
        DownloadService.sendPauseDownloads(
            context,
            AudioDownloadService::class.java,
            false
        )
    }

    override fun resume() {
        DownloadService.sendResumeDownloads(
            context,
            AudioDownloadService::class.java,
            false
        )
    }

    override fun getDownloads(): List<SaveMusic> {
        val result = ArrayList<SaveMusic>()
        val cursor: DownloadCursor = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownloads()

        while (cursor.moveToNext()) {
            result.add(
                SaveMusic(
                    id = cursor.download.request.id,
                    uri = cursor.download.request.uri
                )
            )
        }

        return result
    }

    override fun getDownload(musicId: String): SaveMusic? {
        val download: Download? = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownload(musicId)

        if (download == null) {
            return null
        }

        return SaveMusic(
            id = download.request.id,
            uri = download.request.uri
        )
    }
}