package com.example.musicapp.app.support

import android.annotation.SuppressLint

class ConvertTime {
    @SuppressLint("DefaultLocale")
    fun convertInMinSec(sec: Int): String {
        return String.format(
            "%d:%02d",
            sec / 60 % 60,
            sec % 60
        )
    }
}