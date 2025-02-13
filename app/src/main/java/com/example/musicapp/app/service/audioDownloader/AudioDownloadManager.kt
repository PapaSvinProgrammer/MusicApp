package com.example.musicapp.app.service.audioDownloader

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import java.io.File
import java.util.concurrent.Executor

@UnstableApi
class AudioDownloadManager {
    companion object {
        lateinit var downloadCache: SimpleCache
        lateinit var downloadManager: DownloadManager

        fun init(context: Context) {
            val databaseProvider = StandaloneDatabaseProvider(context)
            val cacheDir = File(context.cacheDir, "music_cache")

            downloadCache = SimpleCache(
                cacheDir,
                NoOpCacheEvictor(),
                databaseProvider
            )

            val dataSourceFactory = DefaultHttpDataSource.Factory()
            val downloadExecutor = Executor(Runnable::run)

            downloadManager = DownloadManager(
                context,
                databaseProvider,
                downloadCache,
                dataSourceFactory,
                downloadExecutor
            )

            downloadManager.maxParallelDownloads = 3
        }
    }
}