package com.example.musicapp.service.audioDownloader

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadCursor
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService

@UnstableApi
class AudioDownloadHelper(
    private val context: Context
) {

    fun download(musicId: String, url: String) {
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

    fun remove(musicId: String) {
        DownloadService.sendRemoveDownload(
            context,
            AudioDownloadService::class.java,
            musicId,
            false
        )
    }

    fun stop() {
        DownloadService.sendPauseDownloads(
            context,
            AudioDownloadService::class.java,
            false
        )
    }

    fun resume() {
        DownloadService.sendResumeDownloads(
            context,
            AudioDownloadService::class.java,
            false
        )
    }

    fun getDownloadedItems(): List<Uri> {
        val result = ArrayList<Uri>()
        val cursor: DownloadCursor = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownloads()

        while (cursor.moveToNext()) {
            val uri = cursor.download.request.uri

            result.add(uri)
        }

        return result
    }
}