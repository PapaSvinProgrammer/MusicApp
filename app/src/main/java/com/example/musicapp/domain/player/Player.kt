package com.example.musicapp.domain.player

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.flac.FlacExtractor
import androidx.media3.extractor.mp3.Mp3Extractor
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.module.AudioPlayer

@UnstableApi
class Player(context: Context) : AudioPlayer {
    private var exoPlayer: ExoPlayer

    init {
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

        val mediaSource = DefaultMediaSourceFactory(context, extractor)

        val exoPlayerBuilder = ExoPlayer.Builder(context)
        exoPlayerBuilder.setRenderersFactory(renderersFactory)
        exoPlayerBuilder.setMediaSourceFactory(mediaSource)

        exoPlayer = exoPlayerBuilder.build()
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

    override fun addNewObjectAndStart(music: Music, isPlay: Boolean) {
        val mediaItem = MediaItem.fromUri(music.url)

        if (music.url.isEmpty()) return

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        if (!isPlay) return

        exoPlayer.playWhenReady = true
    }

    override fun seekTo(msec: Long) {
        exoPlayer.seekTo(msec)
    }
}