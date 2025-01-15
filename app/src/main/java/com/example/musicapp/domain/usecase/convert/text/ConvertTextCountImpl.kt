package com.example.musicapp.domain.usecase.convert.text

private const val MUSIC_TEXT = " трек"
private const val ALBUM_TEXT = " альбом"
private const val PLAYLIST_TEXT = " плейлист"
private const val TIME_TEXT = " час"
private const val DAY_TEXT = " день"

class ConvertTextCountImpl(
    private val convertAnyText: ConvertAnyText
): ConvertTextCount {
    override fun convertMusic(count: Int): String {
        return convertAnyText.execute(count, MUSIC_TEXT)
    }

    override fun convertAlbum(count: Int): String {
        return convertAnyText.execute(count, ALBUM_TEXT)
    }

    override fun convertPlaylist(count: Int): String {
        return convertAnyText.execute(count, PLAYLIST_TEXT)
    }

    override fun convertTime(count: Int): String {
        return convertAnyText.execute(count, TIME_TEXT)
    }
}