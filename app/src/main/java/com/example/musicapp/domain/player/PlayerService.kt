package com.example.musicapp.domain.player

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.R
import com.example.musicapp.domain.module.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerService: Service() {
    companion object {
        const val ACTION_PLAY = "play"
        const val ACTION_PAUSE = "pause"
        const val ACTION_PREV = "prev"
        const val ACTION_NEXT = "next"
        const val ACTION_LIKE = "like"
        const val CHANNEL_ID = "AudioPlayerId"
        const val CHANNEL_NAME = "AudioPlayer"
        const val ACTION_TITLE_PLAY_PAUSE = "play_pause"
        const val ACTION_TITLE_PREV = "prev"
        const val ACTION_TITLE_NEXT = "next"
        const val ACTION_TITLE_LIKE = "like"
        const val TAG = "MusicSession"
    }

    private var mediaPlayer = MediaPlayer()
    private var musicList: List<Music>? = null
    private var isFavorite = MutableLiveData<Boolean>()
    private val currentDuration = MutableLiveData<Float>()
    private val maxDuration = MutableLiveData<Float>()
    private var isPlay = MutableLiveData<Boolean>()
    private var currentPosition = MutableLiveData<Int>()

    override fun onCreate() {
        super.onCreate()
        currentPosition.value = 0
    }

    override fun onBind(intent: Intent?): IBinder {
        return PlayerBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {
                ACTION_PLAY-> play()
                ACTION_PAUSE -> pause()
                ACTION_NEXT -> next()
                ACTION_PREV -> previous()
                ACTION_LIKE -> like()
                else -> {}
            }
        }

        return START_STICKY
    }

    inner class PlayerBinder: Binder() {
        fun getService(): PlayerService = this@PlayerService

        fun setMusicList(list: List<Music>) {
            musicList = list
        }

        fun getCurrentDuration() = this@PlayerService.currentDuration

        fun getMaxDuration() = this@PlayerService.maxDuration

        fun isPlay() = this@PlayerService.isPlay

        fun getCurrentPosition() = this@PlayerService.currentPosition
    }

    fun setPlayerState(state: StatePlayer) {
        when (state) {
            StatePlayer.PLAY -> play()
            StatePlayer.PAUSE -> pause()
            StatePlayer.PREVIOUS -> previous()
            StatePlayer.NEXT -> next()
            StatePlayer.NONE -> {}
        }
    }

    private fun pause() {
        isPlay.value = false
        Log.d("RRRR", "Pause")

        sendNotification(Music(
            "123123",
            "adsfsd",
            "asdasd",
            "asdasd",
            "asdasdasd",
            "asfasfasf",
            "fasfasfas"
        ))
    }

    private fun play() {
        isPlay.value = true
        sendNotification(Music(
            "123123",
            "adsfsd",
            "asdasd",
            "asdasd",
            "asdasdasd",
            "asfasfasf",
            "fasfasfas"
        ))
        Log.d("RRRR", "Play")
    }

    private fun previous() {
        //sendNotification()
        currentPosition.value = (currentPosition.value ?: 0) - 1
        Log.d("RRRR", "prev")
    }

    private fun next() {
        //sendNotification()
        currentPosition.value = (currentPosition.value ?: 0) + 1

        Log.d("RRRR", "next")
    }

    private fun like() {
        isFavorite.value = isFavorite.value != true

        sendNotification(Music(
            "123123",
            "adsfsd",
            "asdasd",
            "asdasd",
            "asdasdasd",
            "asfasfasf",
            "fasfasfas"
        ))
    }

    @SuppressLint("ForegroundServiceType")
    fun sendNotification(music: Music) {
        CoroutineScope(Dispatchers.Main).launch {
            val session = MediaSessionCompat(this@PlayerService, TAG)

            val style = androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2, 3)
                .setMediaSession(session.sessionToken)

            val notification = NotificationCompat.Builder(this@PlayerService, CHANNEL_ID)
                .setStyle(style)
                .setContentTitle(music.name)
                .setContentText(music.group)
                .addAction(
                    R.drawable.ic_skip_previous_fill,
                    ACTION_TITLE_PREV,
                    previousPendingIntent()
                )
                .addAction(
                    if (isPlay.value == true) R.drawable.ic_play else R.drawable.ic_pause,
                    ACTION_TITLE_PLAY_PAUSE,
                    playPendingIntent()
                )
                .addAction(
                    R.drawable.ic_skip_next_fill,
                    ACTION_TITLE_NEXT,
                    nextPendingIntent()
                )
                .addAction(
                    if (isFavorite.value == true) R.drawable.ic_favorite_fill else R.drawable.ic_favorite,
                    ACTION_TITLE_LIKE,
                    likePendingIntent()
                )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.image))
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this@PlayerService, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    with(NotificationManagerCompat.from(this@PlayerService)) {
                        notify(1, notification)
                    }
                }
            } else {
                with(NotificationManagerCompat.from(this@PlayerService)) {
                    notify(1, notification)
                }
            }
        }
    }

    private fun likePendingIntent(): PendingIntent? {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_LIKE
        }

        return PendingIntent.getService(
            this,
            3,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun nextPendingIntent(): PendingIntent? {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_NEXT
        }

        return PendingIntent.getService(
            this,
            2,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun previousPendingIntent(): PendingIntent? {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = ACTION_PREV
        }

        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun playPendingIntent(): PendingIntent? {
        val intent = Intent(this, PlayerService::class.java).apply {
            action = if (isPlay.value == true) ACTION_PAUSE else ACTION_PLAY
        }

        return PendingIntent.getService(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}