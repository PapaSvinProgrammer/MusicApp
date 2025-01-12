package com.example.musicapp.data.module

data class UserYandex (
    val login: String?,
    val id: String?,
    val clientId: String?,
    val emails: ArrayList<String>?,
    val defaultEmail: String?,
    val isAvatarEmpty: Boolean?,
    val defaultAvatarId: String?,
)