package com.example.musicapp.domain.usecase.room.add

import android.icu.util.Calendar
import com.example.musicapp.data.room.playlistEntity.PlaylistEntity
import com.example.musicapp.domain.repository.PlaylistRepository
import java.text.SimpleDateFormat
import java.util.Locale

class AddPlaylistInSQLite(private val playlistRepository: PlaylistRepository) {
    suspend fun execute(name: String, image: String) {
        val date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = simpleDateFormat.format(date)

        playlistRepository.add(
            PlaylistEntity(
                id = 0,
                name = name,
                imageUrl = image,
                date = formattedDate
            )
        )
    }
}