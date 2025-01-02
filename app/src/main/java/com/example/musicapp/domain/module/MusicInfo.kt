package com.example.musicapp.domain.module

import com.google.firebase.firestore.DocumentId

data class MusicInfo(
    @DocumentId var id: String = "",
    var authorMusic: String = "",
    var label: String = "",
    var name: String = "",
    var production: String = "",
    var execute: String = ""
)