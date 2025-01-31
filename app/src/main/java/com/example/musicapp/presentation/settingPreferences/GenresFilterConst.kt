package com.example.musicapp.presentation.settingPreferences

object GenresFilterConst {
    private const val RUSSIAN_ROCK = 31
    private const val INDIE_ROCK = 32
    private const val POP = 23
    private const val HIP_HOP = 12
    private const val ALTERNATIVE_ROCK = 25

    val defaultFilter: List<Int> = arrayListOf(
        RUSSIAN_ROCK,
        INDIE_ROCK,
        ALTERNATIVE_ROCK,
        POP,
        HIP_HOP
    )
}