package com.example.musicapp.service.audioDownloader

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Requirements.NETWORK
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.musicapp.R

@UnstableApi
class AudioDownloadService: DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID,
    R.string.channel_name_download_text,
    R.string.channel_description_download_text
) {
    companion object {
        const val FOREGROUND_NOTIFICATION_ID = 1
        const val CHANNEL_ID = "AudioDownloaderChannelId"
    }

    private lateinit var context: Context
    private lateinit var notificationHelper: DownloadNotificationHelper

    override fun onCreate() {
        super.onCreate()

        context = this
        notificationHelper = DownloadNotificationHelper(
            this,
            CHANNEL_ID
        )
    }

    override fun getDownloadManager(): DownloadManager {
        val manager = AudioManager.audioDownloadManager!!.downloadManager

        manager.addListener(object: DownloadManager.Listener {
            override fun onIdle(downloadManager: DownloadManager) {
                Log.d("RRRR", "IDLE - BLYT")
            }
        })

        return manager
    }

    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return notificationHelper.buildProgressNotification(
            context,
            R.drawable.ic_download,
            null,
            "Message",
            downloads,
            NETWORK
        )
    }
}