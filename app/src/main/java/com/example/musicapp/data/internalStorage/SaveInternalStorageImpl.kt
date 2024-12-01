package com.example.musicapp.data.internalStorage

import android.content.Context
import com.example.musicapp.data.repository.AUDIO_DIRECTORY
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class SaveInternalStorageImpl(
    private val context: Context
) {
    fun execute(array: List<String>) {
        array.forEach { url ->
            saveOnInternalStorage(url)
        }
    }

    private fun saveOnInternalStorage(url: String) {
        val directory = File(context.filesDir, AUDIO_DIRECTORY)
        directory.mkdir()

        val file = File(directory, "abc.mp3")
        file.createNewFile()

        URL(url).openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }
}