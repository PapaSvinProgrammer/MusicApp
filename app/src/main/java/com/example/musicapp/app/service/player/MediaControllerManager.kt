package com.example.musicapp.app.service.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.domain.module.Music
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class MediaControllerManager {
    companion object {
        private var initListener: ((Boolean) -> Unit)? = null
        private var futureMediaController: ListenableFuture<MediaController>? = null
        lateinit var mediaController: MediaController

        fun init(context: Context) {
            val sessionToken = SessionToken(
                context,
                ComponentName(context, MediaService::class.java)
            )

            futureMediaController = MediaController.Builder(context, sessionToken).buildAsync()

            futureMediaController?.addListener({
                futureMediaController?.let {
                    mediaController = it.get()
                    initListener?.invoke(true)
                }
            }, MoreExecutors.directExecutor())
        }

        fun setMediaItems(list: List<Music>) {
            val mediaItems = list.map {
                MediaItem.Builder()
                    .setUri(it.url)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(it.name)
                            .setArtist(it.group)
                            .setArtworkUri(Uri.parse(it.imageLow))
                            .build()
                    ).build()
            }.toList()

            PlayerInfo.setMusicList(list)
            mediaController.setMediaItems(mediaItems, true)
            mediaController.prepare()
        }

        fun putRandomMusic(music: Music?) {
            val mediaItem = MediaItem.Builder()
                .setUri(music?.url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(music?.group)
                        .setTitle(music?.name)
                        .setArtworkUri(Uri.parse(music?.imageLow))
                        .build()
                )
                .build()

            music?.let { PlayerInfo.putItem(music) }
            mediaController.addMediaItem(mediaItem)
            mediaController.prepare()
        }

        fun currentDuration() = flow {
            while (mediaController.isPlaying) {
                emit(mediaController.currentPosition)
                delay(500)
            }
        }

        fun getCurrentMusic(): Music {
            var currentObject: Music

            try {
                currentObject = PlayerInfo.musicList.value!![mediaController.currentMediaItemIndex]
            } catch (e: IndexOutOfBoundsException) {
                currentObject = PlayerInfo.musicList.value!![0]
            } catch (e: NullPointerException) {
                currentObject = Music()
            }

            return currentObject
        }

        fun setCurrentPosition(position: Int) {
            if (position == mediaController.currentMediaItemIndex) {
                return
            }

            mediaController.seekToDefaultPosition(position)
        }

        fun addInQueue() {

        }

        fun addInNext() {

        }

        fun release() {
            futureMediaController?.let { MediaController.releaseFuture(it) }
        }

        fun addInitCallback(initListener: (Boolean) -> Unit) {
            this.initListener = initListener
        }
    }
}