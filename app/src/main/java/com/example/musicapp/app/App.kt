package com.example.musicapp.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.data.repository.SharedPreferencesRepositoryImpl
import com.example.musicapp.di.appModule
import com.example.musicapp.di.dataModule
import com.example.musicapp.di.domainModule
import com.example.musicapp.service.audioDownloader.AudioDownloadManager
import com.example.musicapp.service.audioDownloader.AudioDownloadService
import com.example.musicapp.service.audioDownloader.AudioManager
import com.example.musicapp.service.player.PlayerService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        AudioManager.audioDownloadManager = AudioDownloadManager(
            this,
            SharedPreferencesRepositoryImpl(this)
        )

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, dataModule, domainModule)
        }

        createPlayerNotification()
        createDownloaderNotification()
    }

    @OptIn(UnstableApi::class)
    private fun createDownloaderNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AudioDownloadService.CHANNEL_ID,
                getString(R.string.channel_name_download_text),
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createPlayerNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PlayerService.CHANNEL_ID,
                PlayerService.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}