package com.example.musicapp.app.service.audioDownloader

import android.content.Context
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import com.example.musicapp.data.constant.ErrorConst
import com.google.firebase.Firebase
import java.io.File
import java.util.concurrent.Executor

@UnstableApi
class AudioDownloadManager(
    context: Context
) {
    var downloadManager: DownloadManager
    var downloadCache: SimpleCache? = null

    init {
        val databaseProvider = StandaloneDatabaseProvider(context)
        val cacheDir = File(context.cacheDir, "music_cache")

        try {
            downloadCache = SimpleCache(
                cacheDir,
                NoOpCacheEvictor(),
                databaseProvider
            )
        } catch (e: Exception) {
            Log.d(ErrorConst.DEFAULT_ERROR, e.message.toString())
        }

        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val downloadExecutor = Executor(Runnable::run)

        downloadManager = DownloadManager(
            context,
            databaseProvider,
            downloadCache!!,
            dataSourceFactory,
            downloadExecutor
        )

        downloadManager.maxParallelDownloads = 3
    }
}