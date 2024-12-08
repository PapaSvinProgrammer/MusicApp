package com.example.musicapp.domain.player.module

import android.app.Service
import androidx.lifecycle.MutableLiveData

interface AudioBinder {
    fun getService(): Service
    fun getCurrentDuration(): MutableLiveData<Int>
    fun getMaxDuration(): MutableLiveData<Int>
    fun isPlay(): MutableLiveData<Boolean>
    fun isRepeat(): MutableLiveData<Boolean>
    fun getCurrentPosition(): MutableLiveData<Int>
}