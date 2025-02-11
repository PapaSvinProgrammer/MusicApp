package com.example.musicapp.app.service.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object DataPlayerType {
    private val typeLiveData = MutableLiveData<TypeDataPlayer>()
    val type: LiveData<TypeDataPlayer> = typeLiveData

    fun setType(type: TypeDataPlayer) {
        typeLiveData.value = type
    }
}