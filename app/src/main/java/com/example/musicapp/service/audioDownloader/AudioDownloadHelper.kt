package com.example.musicapp.service.audioDownloader

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadCursor
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.DownloadMusicRepository
import com.google.gson.Gson

@UnstableApi
class AudioDownloadHelper(
    private val context: Context
): DownloadMusicRepository {

    override fun download(music: Music) {
        val json = Gson().toJson(music)

        val downloadRequest = DownloadRequest.Builder(
            music.id.toString(),
            music.url.toString().toUri()
        ).setData(Util.getUtf8Bytes(json.toString()))
            .build()

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

    override fun getDownloads(): List<Music> {
        return getDownloadsLimit(0)
    }

    override fun getDownloadsLimit(limit: Int): List<Music> {
        val result = ArrayList<Music>()
        var count = 0
        val cursor: DownloadCursor = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownloads()

        while (cursor.moveToNext()) {
            val jsonString = Util.fromUtf8Bytes(cursor.download.request.data)

            val music = Gson().fromJson(jsonString, Music::class.java)
            result.add(music)

            count++
            if (limit != 0 && count == limit) {
                cursor.close()
                return result
            }
        }

        cursor.close()
        return result
    }

    override fun getDownload(musicId: String): Music? {
        val download: Download? = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownload(musicId)

        if (download == null) {
            return null
        }

        val jsonString = Util.fromUtf8Bytes(download.request.data)

        return Gson().fromJson(jsonString, Music::class.java)
    }

    override fun getCount(): Int {
        val cursor: DownloadCursor = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownloads()
        val result = cursor.count

        cursor.close()
        return result
    }

    override fun search(text: String): List<Music> {
        val result = ArrayList<Music>()
        var count = 0
        val cursor: DownloadCursor = AudioManager.audioDownloadManager
            .downloadManager
            .downloadIndex
            .getDownloads()

        while (cursor.moveToNext()) {
            val jsonString = Util.fromUtf8Bytes(cursor.download.request.data)
            val music = Gson().fromJson(jsonString, Music::class.java)

            if ((music.name + music.albumName + music.group).lowercase().trim().contains(text.lowercase().trim())) {
                result.add(music)
                count++
            }
        }

        cursor.close()
        return result
    }
}