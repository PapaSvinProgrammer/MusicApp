package com.example.musicapp.domain.module

import android.net.Uri

data class SaveMusic(
    val id: String,
    val uri: Uri
)