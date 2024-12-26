package com.example.musicapp.domain.player.module

import android.app.Service
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.domain.module.Music

interface AudioBinder {
    fun getService(): Service
    fun getCurrentDuration(): MutableLiveData<Long>
    fun getMaxDuration(): MutableLiveData<Long>
    fun isPlay(): MutableLiveData<Boolean>
    fun isRepeat(): MutableLiveData<Boolean>
    fun getCurrentPosition(): MutableLiveData<Int>
}