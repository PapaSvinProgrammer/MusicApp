package com.example.musicapp.domain.player.module

import android.app.Service
import androidx.lifecycle.MutableLiveData

interface AudioBinder {
    fun getService(): Service
    fun getCurrentDuration(): MutableLiveData<Float>
    fun getMaxDuration(): MutableLiveData<Float>
    fun isPlay(): MutableLiveData<Boolean>
    fun getCurrentPosition(): MutableLiveData<Int>
}