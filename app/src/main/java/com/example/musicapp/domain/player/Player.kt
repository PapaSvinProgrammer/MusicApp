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

    override fun getBufferedPosition(): Long {
        return exoPlayer.bufferedPosition
    }

    override fun getCurrentItem(): Int {
        return exoPlayer.currentMediaItemIndex
    }

    override fun seekTo(msec: Long) {
        exoPlayer.seekTo(msec)
    }

    override fun setList(list: List<Music>) {
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
}