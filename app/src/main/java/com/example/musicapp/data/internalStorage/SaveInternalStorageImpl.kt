package com.example.musicapp.data.internalStorage

import android.content.Context
import com.example.musicapp.domain.module.Music
import java.io.File
import java.io.FileOutputStream
import java.net.URL

private const val AUDIO_DIRECTORY = "Musics"

class SaveInternalStorageImpl(
    private val context: Context
) {
    fun execute(array: List<Music>) {
        array.forEach { music ->
            saveOnInternalStorage(music)
        }
    }

    private fun saveOnInternalStorage(item: Music) {
        val directory = File(context.filesDir, AUDIO_DIRECTORY)
        directory.mkdir()

        val file = File(directory, item.id.toString())
        file.createNewFile()

        URL(item.url).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }
}