package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.SaveMusic

interface DownloadMusicRepository {
    fun download(musicId: String, url: String)
    fun remove(musicId: String)
    fun stop()
    fun resume()
    fun getDownloads(): List<SaveMusic>
    fun getDownload(musicId: String): SaveMusic?
}