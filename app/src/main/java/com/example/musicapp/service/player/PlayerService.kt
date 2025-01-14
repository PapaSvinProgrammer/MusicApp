package com.example.musicapp.service.player

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.module.AudioPlayer
import com.example.musicapp.domain.state.StatePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val COUNT_MSEC_TO_RESET = 3000

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
    private var job: Job? = null
    private var lastIndexToQueue = 0

    private var musicList = MutableLiveData<List<Music>>()
    private val addMusic = MutableLiveData<Music>()
    private var isFavorite = MutableLiveData<Boolean>()
    private val currentDuration = MutableLiveData<Long>()
    private val maxDuration = MutableLiveData<Long>()
    private val bufferedPosition = MutableLiveData<Long>()
    private var isPlay = MutableLiveData<Boolean>()
    private val isRepeat = MutableLiveData<Boolean>()
    private var currentPosition = MutableLiveData<Int>()
    private val currentObject = MutableLiveData<Music>()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        Log.d("RRRR", "Create service")
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

        audioPlayer = Player(applicationContext)
    }

    override fun onBind(intent: Intent?): IBinder {
        return PlayerBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Main).launch {
            intent?.let {
                when (intent.action) {
                    ACTION_PLAY -> play()
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

    inner class PlayerBinder: Binder() {
        fun getService(): PlayerService = this@PlayerService

        fun getCurrentDuration() = this@PlayerService.currentDuration

        fun getMaxDuration() = this@PlayerService.maxDuration

        fun isPlay() = this@PlayerService.isPlay

        fun isRepeat() = this@PlayerService.isRepeat

        fun getCurrentPosition() = this@PlayerService.currentPosition

        fun getBufferedPosition() = this@PlayerService.bufferedPosition

        fun getCurrentObject() = this@PlayerService.currentObject

        fun getMusicList() = this@PlayerService.musicList

    }

    fun setPlayerState(state: StatePlayer) {
        CoroutineScope(Dispatchers.Main).launch {
            when (state) {
                StatePlayer.PLAY -> play()
                StatePlayer.PAUSE -> pause()
                else -> {}
            }
        }
    }

    fun setMusicList(list: List<Music>) {
        this.musicList.value = list

        currentObject.value = musicList.value!![currentPosition.value ?: 0]
        audioPlayer?.setData(list)
    }

    fun addMusic(music: Music) {
        addMusic.value = music
        audioPlayer?.addMusic(music)

        val currentList = musicList.value?.toMutableList()
        currentList?.add(music)
        musicList.value = currentList ?: listOf()
    }

    fun playNext(music: Music) {
        audioPlayer?.addNext(music)

        val currentPosition = currentPosition.value ?: 0
        val currentList = musicList.value?.toMutableList()
        currentList?.add(currentPosition + 1, music)

        musicList.value = currentList ?: listOf()
    }

    fun addToQueue(music: Music) {
        audioPlayer?.addInQueue(music)

        if ((currentPosition.value ?: 0) > lastIndexToQueue) {
            lastIndexToQueue = (currentPosition.value ?: 0) + 1
        }
        else {
            lastIndexToQueue++
        }

        val currentMusicList = musicList.value?.toMutableList()

        if (lastIndexToQueue > (musicList.value?.size ?: 0)) {
            currentMusicList?.add(music)
        }
        else {
            currentMusicList?.add(lastIndexToQueue, music)
        }

        musicList.value = currentMusicList ?: listOf()
    }

    fun setCurrentPosition(position: Int) {
        if (position == audioPlayer?.getCurrentItem()) {
            return
        }

        job?.cancel()

        if (musicList.value == null) return

        currentObject.value = musicList.value!![position]
        currentPosition.value = position

        audioPlayer?.setPosition(position, isPlay.value ?: false)

        maxDuration.value = 0
        updateDurations()

        audioNotification?.execute(currentObject.value!!)
    }

    fun seekTo(msec: Int) {
        audioPlayer?.seekTo(msec.toLong())
    }

    fun reset() {
        audioPlayer?.reset()
    }

    fun like() {
        isFavorite.value = isFavorite.value != true
    }

    fun repeat(state: Boolean) {
        isRepeat.value = state
        audioPlayer?.repeat(state)
    }

    fun shuffle() {
        audioPlayer?.shuffle()
    }

    private fun pause() {
        isPlay.value = false
        audioPlayer?.pause()

        if (musicList.value != null) {
            audioNotification?.execute(currentObject.value ?: Music())
        }
    }

    private fun play() {
        isPlay.value = true

        if (musicList.value == null) return

        audioPlayer?.play()
        updateDurations()

        audioNotification?.execute(currentObject.value ?: Music())
    }

    fun previous() {
        if ((currentPosition.value ?: 0) - 1 < 0) return

        if (isRepeat.value == true) {
            return
        }

        if ((currentDuration.value ?: 0) > COUNT_MSEC_TO_RESET) {
            reset()
            return
        }

        currentPosition.value = (currentPosition.value ?: 0) - 1
        setCurrentPosition(currentPosition.value ?: 0)
    }

    fun next() {
        if (isRepeat.value == true) {
            return
        }

        currentPosition.value = (currentPosition.value ?: 0) + 1
        setCurrentPosition(currentPosition.value ?: 0)
    }

    private fun updateDurations() {
        job?.cancel()

        job = CoroutineScope(Dispatchers.Main).launch {
            while (isPlay.value == true) {
                if ((maxDuration.value ?: 0) <= 0L) {
                    maxDuration.value = audioPlayer?.getMaxDuration()
                }

                currentDuration.value = audioPlayer?.getCurrentDuration()
                bufferedPosition.value = audioPlayer?.getBufferedPosition()

                if ((currentPosition.value ?: 0) != audioPlayer?.getCurrentItem()) {
                    currentObject.value = musicList.value!![currentPosition.value ?: 0]
                    currentPosition.value = audioPlayer?.getCurrentItem()
                    maxDuration.value = 0
                }

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

    override fun onDestroy() {
        Log.d("RRRR", "Destroy Service")
        super.onDestroy()
    }
}