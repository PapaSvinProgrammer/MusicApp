package com.example.musicapp.domain.module


data class Group (
    var name: String?,
    var albums: ArrayList<String>?,
    var compound: ArrayList<String>?,
    var genres: ArrayList<String>?,
    var country: String?,
    var musics: ArrayList<String>?,
    var year: String?,
    var image: String?,
    var isFavorite: Boolean? = null
)