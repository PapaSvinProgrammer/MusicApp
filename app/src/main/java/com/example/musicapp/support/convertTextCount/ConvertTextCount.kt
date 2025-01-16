package com.example.musicapp.support.convertTextCount

interface ConvertTextCount {
    fun convertMusic(count: Int): String
    fun convertAlbum(count: Int): String
    fun convertPlaylist(count: Int): String
    fun convertTime(count: Int): String
}