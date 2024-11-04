package com.example.musicapp.domain.module

import java.io.Serializable

data class Group (
    var id: String,
    var albums: ArrayList<String>,
    var compound: ArrayList<String>,
    var genres: ArrayList<String>,
    var country: String,
    var musics: ArrayList<String>,
    var year: String,
    var image: String
): Serializable