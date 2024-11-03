package com.example.musicapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.module.LoginData

interface SignAndCreateRepository {
    fun signWithEmailAndPassword(data: LoginData): LiveData<String?>
    fun createWithEmailAndPassword(data: LoginData): LiveData<String?>
}