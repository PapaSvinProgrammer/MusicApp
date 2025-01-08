package com.example.musicapp.service.player

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toIcon
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.musicapp.R
import com.example.musicapp.domain.module.Music
import com.example.musicapp.service.player.PlayerService.Companion.ACTION_TITLE_LIKE
import com.example.musicapp.service.player.PlayerService.Companion.ACTION_TITLE_NEXT
import com.example.musicapp.service.player.PlayerService.Companion.ACTION_TITLE_PLAY_PAUSE
import com.example.musicapp.service.player.PlayerService.Companion.ACTION_TITLE_PREV
import com.example.musicapp.service.player.PlayerService.Companion.CHANNEL_ID
import com.example.musicapp.service.player.PlayerService.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioNotification(
    private val context: Context,
    private val isPlay: MutableLiveData<Boolean>,
    private val isFavorite: MutableLiveData<Boolean>,
    private val nextPendingIntent: PendingIntent?,
    private val prevPendingIntent: PendingIntent?,
    private val playPendingIntent: PendingIntent?,
    private val likePendingIntent: PendingIntent?
) {

    fun execute(music: Music) {
        Glide.with(context)
            .asBitmap()
            .load(music.imageHigh)
            .apply(
                RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL)
            )
            .listener(object: RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                    return false
                }

                @SuppressLint("NewApi")
                override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    CoroutineScope(Dispatchers.Main).launch {
                        drawNotification(
                            image = resource.toIcon(),
                            music = music
                        )
                    }

                    return false
                }
            })
            .submit()
    }

    private fun drawNotification(image: Icon, music: Music) {
        val session = MediaSessionCompat(context, TAG)

        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2, 3)
            .setMediaSession(session.sessionToken)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(music.name)
            .setContentText(music.group)
            .addAction(
                R.drawable.ic_skip_previous_fill,
                ACTION_TITLE_PREV,
                prevPendingIntent
            )
            .addAction(
                if (isPlay.value == true) R.drawable.ic_pause else R.drawable.ic_play,
                ACTION_TITLE_PLAY_PAUSE,
                playPendingIntent
            )
            .addAction(
                R.drawable.ic_skip_next_fill,
                ACTION_TITLE_NEXT,
                nextPendingIntent
            )
            .addAction(
                if (isFavorite.value == true) R.drawable.ic_favorite_fill else R.drawable.ic_favorite,
                ACTION_TITLE_LIKE,
                likePendingIntent
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(image)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                with(NotificationManagerCompat.from(context)) {
                    notify(1, notification)
                }
            }
        } else {
            with(NotificationManagerCompat.from(context)) {
                notify(1, notification)
            }
        }
    }
}