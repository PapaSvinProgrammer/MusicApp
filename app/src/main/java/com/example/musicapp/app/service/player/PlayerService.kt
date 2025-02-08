package com.example.musicapp.app.service.player

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.domain.module.Music
import com.example.musicapp.app.service.player.module.AudioPlayer
import com.example.musicapp.domain.state.StatePlayer
import com.example.musicapp.app.service.player.module.PlayerInfo
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
    private val isRepeat = MutableLiveData<Boolean>()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        PlayerInfo.setIsPlay(false)
        PlayerInfo.setCurrentPosition(0)

        maxDuration.value = 0

        audioNotification = AudioNotification(
            context = this@PlayerService,
            isFavorite = isFavorite,
            nextPendingIntent = nextPendingIntent(),
            prevPendingIntent = previousPendingIntent(),
            playPendingIntent = playPendingIntent(),
            likePendingIntent = likePendingIntent()
        )

        audioPlayer = PlayerImpl(
            context = applicationContext,
            listener = playerListener
        )
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

        fun isRepeat() = this@PlayerService.isRepeat

        fun getBufferedPosition() = this@PlayerService.bufferedPosition

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
        if (list == musicList.value) {
            return
        }

        this.musicList.value = list

        updateCurrentObject()
        audioPlayer?.setData(list)
    }

    fun setDownloadMusicList(list: List<Music>) {
        if (list == musicList.value) {
            return
        }

        this.musicList.value = list

        updateCurrentObject()
        audioPlayer?.setDataDownload(list)
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

        val currentPosition = PlayerInfo.currentPosition.value ?: 0
        val currentList = musicList.value?.toMutableList()
        currentList?.add(currentPosition + 1, music)

        musicList.value = currentList ?: listOf()
    }

    fun addToQueue(music: Music) {
        audioPlayer?.addInQueue(music)

        if ((PlayerInfo.currentPosition.value ?: 0) > lastIndexToQueue) {
            lastIndexToQueue = (PlayerInfo.currentPosition.value ?: 0) + 1
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

        if (musicList.value == null) {
            return
        }

        audioPlayer?.setPosition(
            position = position,
            isPlay = PlayerInfo.isPlay.value ?: false
        )
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

    fun previous() {
        if ((currentDuration.value ?: 0) > COUNT_MSEC_TO_RESET) {
            reset()
            return
        }

        if ((PlayerInfo.currentPosition.value ?: 0) - 1 < 0) {
            return
        }

        if (isRepeat.value == true) {
            return
        }

        val currentPosition = (PlayerInfo.currentPosition.value ?: 0) - 1
        setCurrentPosition(currentPosition)
    }

    fun next() {
        if ((PlayerInfo.currentPosition.value ?: 0) + 1 > (musicList.value?.size ?: 0) - 1) {
            return
        }

        if (isRepeat.value == true) {
            return
        }

        val currentPosition = (PlayerInfo.currentPosition.value ?: 0) + 1
        setCurrentPosition(currentPosition)
    }

    private fun pause() {
        PlayerInfo.setIsPlay(false)
        audioPlayer?.pause()
    }

    private fun play() {
        PlayerInfo.setIsPlay(true)
        audioPlayer?.play()
    }

    private fun updateCurrentObject() {
        val currentObject = musicList.value!![0]
        PlayerInfo.setCurrentObject(currentObject)
    }

    private fun updateDurations() {
        job?.cancel()

        job = CoroutineScope(Dispatchers.Main).launch {
            while (PlayerInfo.isPlay.value == true) {
                maxDuration.value = audioPlayer?.getMaxDuration()
                currentDuration.value = audioPlayer?.getCurrentDuration()
                bufferedPosition.value = audioPlayer?.getBufferedPosition()
                delay(1000)
            }
        }
    }

    private val playerListener = object: Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val currentItemPosition = audioPlayer?.getCurrentItem() ?: 0

            try {
                val currentObject = musicList.value!![currentItemPosition]
                PlayerInfo.setCurrentObject(currentObject)
            } catch (e: Exception) {
                PlayerInfo.setCurrentObject(Music())
            }

            PlayerInfo.setCurrentPosition(currentItemPosition)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            audioNotification?.execute(PlayerInfo.currentObject.value ?: Music())
            updateDurations()
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
            action = if (PlayerInfo.isPlay.value == true) ACTION_PAUSE else ACTION_PLAY
        }

        return PendingIntent.getService(
            this,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onDestroy() {
        audioPlayer?.release()

        super.onDestroy()
    }
}