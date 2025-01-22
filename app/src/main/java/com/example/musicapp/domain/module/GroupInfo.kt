package com.example.musicapp.domain.module

data class GroupInfo(
    val connectionUrl: Map<String, String>? = null,
    val images: List<String>? = null,
    val info: String? = null,
    val name: String? = null
)