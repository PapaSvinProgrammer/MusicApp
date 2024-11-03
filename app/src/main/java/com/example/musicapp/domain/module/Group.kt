package com.example.musicapp.domain.module

data class Group (
    val id: String,
    val albums: Array<String>,
    val compound: Array<String>,
    val genres: Array<String>,
    val country: String,
    val musics: Array<String>,
    val year: String
)