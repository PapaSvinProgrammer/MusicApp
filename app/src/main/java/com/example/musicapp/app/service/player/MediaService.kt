package com.example.musicapp.app.service.player

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MediaService: MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        CreateExoPLayer.create(this)
        val player = CreateExoPLayer.exoPlayer
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
}