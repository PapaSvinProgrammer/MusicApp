package com.example.musicapp.data.constant

object GenresConst {
    private const val RUSSIAN_ROCK = "русский рок"
    private const val INDIE_ROCK = "инди"
    private const val POP = "поп"
    private const val HIP_HOP = "хип-хоп"
    private const val RB = "R&B"
    private const val CLASSIC = "классическая"
    private const val SCHANSON = "шансон"
    private const val TRANS = "транс"
    private const val HAUSE = "хаус"
    private const val ALTERNATIVE_ROCK = "альтернатива"

    val array: List<String> = arrayListOf(
        RUSSIAN_ROCK,
        INDIE_ROCK,
        ALTERNATIVE_ROCK,
        POP,
        HIP_HOP,
        RB,
        CLASSIC,
        SCHANSON,
        TRANS,
        HAUSE
    )
}