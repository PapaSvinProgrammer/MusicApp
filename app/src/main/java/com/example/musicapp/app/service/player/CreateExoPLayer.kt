package com.example.musicapp.app.service.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.flac.FlacExtractor
import androidx.media3.extractor.mp3.Mp3Extractor
import com.example.musicapp.app.service.audioDownloader.AudioDownloadManager

class CreateExoPLayer {
    companion object {
        lateinit var exoPlayer: ExoPlayer

        @OptIn(UnstableApi::class)
        fun create(context: Context) {
            val extractor = ExtractorsFactory {
                arrayOf(
                    Mp3Extractor(),
                    FlacExtractor()
                )
            }

            val renderersFactory = RenderersFactory { eventHandler, _, rendererListener, _, _ ->
                arrayOf(
                    MediaCodecAudioRenderer(
                        context,
                        MediaCodecSelector.DEFAULT,
                        eventHandler,
                        rendererListener
                    )
                )
            }

            val cacheDataSourceFactory: DataSource.Factory =
                CacheDataSource.Factory()
                    .setCache(AudioDownloadManager.downloadCache)
                    .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
                    .setCacheWriteDataSinkFactory(null)
            val mediaSource = DefaultMediaSourceFactory(context, extractor)
                .setDataSourceFactory(cacheDataSourceFactory)

            val exoPlayerBuilder = ExoPlayer.Builder(context)
            exoPlayerBuilder.setRenderersFactory(renderersFactory)
            exoPlayerBuilder.setMediaSourceFactory(mediaSource)

            exoPlayer = exoPlayerBuilder.build()
        }
    }
}