package com.example.musicapp.service.audioDownloader

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import com.example.musicapp.domain.repository.SharedPreferencesRepository
import java.io.File
import java.util.concurrent.Executor

@UnstableApi
class AudioDownloadManager(
    context: Context,
    sharedPreferencesRepository: SharedPreferencesRepository
) {
    var downloadManager: DownloadManager
    var downloadCache: SimpleCache

    init {
        val userId = sharedPreferencesRepository.getUserKey()
        val databaseProvider = StandaloneDatabaseProvider(context)
        val cacheDir = File(context.getExternalFilesDir(null), userId)

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