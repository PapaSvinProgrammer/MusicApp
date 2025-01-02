package com.example.musicapp.domain.module

import com.google.firebase.firestore.DocumentId

data class MusicText (
    @DocumentId var id: String? = null,
    var text: String? = null
)