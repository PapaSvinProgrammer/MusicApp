package com.example.musicapp.domain.repository

import com.example.musicapp.domain.module.Music

interface DownloadMusicRepository {
    fun download(music: Music)
    fun remove(musicId: String)
    fun stop()
    fun resume()
    fun getDownloads(): List<Music>
    fun getDownloadsLimit(limit: Int): List<Music>
    fun getDownload(musicId: String): Music?
    fun getCount(): Int
}