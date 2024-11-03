package com.example.musicapp.domain.module

import java.util.Date

data class Album (
    val id: String,
    val date: Date,
    val genre: Array<String>,
    val image: String,
    val name: String,
    val time: String
)