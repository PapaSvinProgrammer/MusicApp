package com.example.musicapp.domain.player

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.player.module.AudioBinder
import com.example.musicapp.domain.player.module.AudioPlayer
import com.example.musicapp.domain.player.state.StatePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    private var audioNotification: AudioNotification? = null
    private var audioPlayer: AudioPlayer? = null
    private var currentObject: Music? = null
    private var musicList: List<Music>? = null

    private var isFavorite = MutableLiveData<Boolean>()
    private val currentDuration = MutableLiveData<Int>()
    private val maxDuration = MutableLiveData<Int>()
    private var isPlay = MutableLiveData<Boolean>()
    private var currentPosition = MutableLiveData<Int>()

    override fun onCreate() {
        super.onCreate()

        isPlay.value = false
        currentPosition.value = 0
        maxDuration.value = 0

        audioNotification = AudioNotification(
            context = this@PlayerService,
            isPlay = isPlay,
            isFavorite = isFavorite,
            nextPendingIntent = nextPendingIntent(),
            prevPendingIntent = previousPendingIntent(),
            playPendingIntent = playPendingIntent(),
            likePendingIntent = likePendingIntent()
        )

        audioPlayer = Player()
    }

    override fun onBind(intent: Intent?): IBinder {
        return PlayerBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Main).launch {
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
        }

        return START_STICKY
    }

    inner class PlayerBinder: Binder(), AudioBinder {
        override fun getService(): PlayerService = this@PlayerService

        override fun getCurrentDuration() = this@PlayerService.currentDuration

        override fun getMaxDuration() = this@PlayerService.maxDuration

        override fun isPlay() = this@PlayerService.isPlay

        override fun getCurrentPosition() = this@PlayerService.currentPosition
    }

    fun setPlayerState(state: StatePlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {
                StatePlayer.PLAY -> play()
                StatePlayer.PAUSE -> pause()
                StatePlayer.PREVIOUS -> previous()
                StatePlayer.NEXT -> next()
                StatePlayer.NONE -> {}
            }
        }
    }

    fun setMusicList(list: List<Music>) {
        this.musicList = list
    }

    fun seekTo(msec: Int) {
        audioPlayer?.seekTo(msec)
    }

    private fun pause() {
        isPlay.value = false
        audioPlayer?.pause()

        if (musicList != null) {
            audioNotification?.execute(musicList!![currentPosition.value ?: 0])
        }
    }

    private fun play() {
        isPlay.value = true

        Log.d("RRRR", "List = " + musicList)
        if (currentObject == null) {
            currentObject = musicList!![currentPosition.value ?: 0]

            audioPlayer?.addNewObjectAndStart(
                music = currentObject!!,
                isPlay = isPlay.value ?: false
            )
        }
        else {
            audioPlayer?.play()
        }

        updateDurations()

        if (musicList != null) {
            audioNotification?.execute(musicList!![currentPosition.value ?: 0])
        }
    }

    private fun previous() {
        if ((currentPosition.value ?: 0) - 1 < 0) return

        currentPosition.value = (currentPosition.value ?: 0) - 1
        currentObject = musicList!![currentPosition.value ?: 0]

        audioPlayer?.addNewObjectAndStart(
            music = currentObject!!,
            isPlay = isPlay.value ?: false
        )

        updateDurations()
        audioNotification?.execute(musicList!![currentPosition.value ?: 0])

        Log.d("RRRR", "prev")
    }

    private fun next() {
        currentPosition.value = (currentPosition.value ?: 0) + 1
        currentObject = musicList!![currentPosition.value ?: 0]

        audioPlayer?.addNewObjectAndStart(
            music = currentObject!!,
            isPlay = isPlay.value ?: false
        )

        updateDurations()
        audioNotification?.execute(musicList!![currentPosition.value ?: 0])

        Log.d("RRRR", "next")
    }

    private fun like() {
        isFavorite.value = isFavorite.value != true
    }

    private fun updateDurations() {
        maxDuration.value = 0

        CoroutineScope(Dispatchers.Main).launch {
            while (isPlay.value == true) {
                if (maxDuration.value == 0) maxDuration.value = audioPlayer?.getMaxDuration()

                currentDuration.value = audioPlayer?.getCurrentDuration()
                delay(1000)
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