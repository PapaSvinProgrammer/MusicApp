package com.example.musicapp.app.service.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
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
import com.example.musicapp.domain.module.Music
import com.example.musicapp.app.service.audioDownloader.AudioManager
import com.example.musicapp.app.service.player.module.AudioPlayer

private const val COUNT_MSEC_TO_RESET = 3000

@UnstableApi
class PlayerImpl(
    private val context: Context,
    private val listener: Listener? = null
) : AudioPlayer {
    private lateinit var exoPlayer: ExoPlayer
    private var lastIndexInQueue = 0
    private var defaultPlayer = true

    init {
        initDefaultPlayer()
    }

    private fun initDefaultPlayer() {
        defaultPlayer = true

        val dataSourceFactory = DefaultHttpDataSource.Factory()

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

        val mediaSource = DefaultMediaSourceFactory(
            dataSourceFactory,
            extractor
        )

        val exoPlayerBuilder = ExoPlayer.Builder(context)
        exoPlayerBuilder.setRenderersFactory(renderersFactory)
        exoPlayerBuilder.setMediaSourceFactory(mediaSource)

        exoPlayer = exoPlayerBuilder.build()

        listener?.apply {
            exoPlayer.addListener(this)
        }
    }

    private fun initDownloadPlayer() {
        defaultPlayer = false

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
                .setCache(AudioManager.audioDownloadManager!!.downloadCache!!)
                .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
                .setCacheWriteDataSinkFactory(null)

        val mediaSource = DefaultMediaSourceFactory(context, extractor)
            .setDataSourceFactory(cacheDataSourceFactory)

        val exoPlayerBuilder = ExoPlayer.Builder(context)
        exoPlayerBuilder.setRenderersFactory(renderersFactory)
        exoPlayerBuilder.setMediaSourceFactory(mediaSource)

        exoPlayer = exoPlayerBuilder.build()

        listener?.apply {
            exoPlayer.addListener(this)
        }
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun reset() {
        exoPlayer.seekTo(0)
    }

    override fun repeat(state: Boolean) {
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
    }

    override fun getCurrentDuration():Long {
        return exoPlayer.currentPosition
    }

    override fun getMaxDuration(): Long {
        return exoPlayer.duration
    }

    override fun getBufferedPosition(): Long {
        return exoPlayer.bufferedPosition
    }

    override fun getCurrentItem(): Int {
        return exoPlayer.currentMediaItemIndex
    }

    override fun seekTo(msec: Long) {
        exoPlayer.seekTo(msec)
    }

    override fun setData(list: List<Music>) {
        if (!defaultPlayer) {
            exoPlayer.release()
            initDefaultPlayer()
        }

        val mediaItems = list.asSequence().map {
            MediaItem.fromUri(it.url.toString())
        }.toList()

        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    override fun setDataDownload(list: List<Music>) {
        if (defaultPlayer) {
            exoPlayer.release()
            initDownloadPlayer()
        }

        val mediaItems = list.asSequence().map {
            MediaItem.fromUri(it.url.toString())
        }.toList()

        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    override fun setPosition(position: Int, isPlay: Boolean) {
        exoPlayer.seekToDefaultPosition(position)

        if (!isPlay) return

        exoPlayer.play()
    }

    override fun shuffle() {
        exoPlayer.shuffleModeEnabled = true
    }

    override fun addMusic(music: Music) {
        val mediaItem = MediaItem.fromUri(music.url.toString())

        exoPlayer.addMediaItem(mediaItem)
    }

    override fun addInQueue(music: Music) {
        val mediaItem = MediaItem.fromUri(music.url.toString())

        if (exoPlayer.currentMediaItemIndex > lastIndexInQueue) {
            lastIndexInQueue = exoPlayer.currentMediaItemIndex + 1
        }
        else {
            lastIndexInQueue++
        }

        if (lastIndexInQueue > exoPlayer.mediaItemCount) {
            exoPlayer.addMediaItem(mediaItem)
        }
        else {
            exoPlayer.addMediaItem(lastIndexInQueue, mediaItem)
        }
    }

    override fun addNext(music: Music) {
        val mediaItem = MediaItem.fromUri(music.url.toString())
        val currentPosition = exoPlayer.currentMediaItemIndex

        exoPlayer.addMediaItem(currentPosition + 1, mediaItem)
    }

    override fun release() {
        exoPlayer.release()
    }
}