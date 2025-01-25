package com.example.musicapp.domain.module

import com.google.firebase.firestore.DocumentId

data class GroupInfo(
    @DocumentId
    val id: String? = null,
    val connectionUrl: Map<String, String>? = null,
    val images: List<String>? = null,
    val info: String? = null,
    val name: String? = null
)