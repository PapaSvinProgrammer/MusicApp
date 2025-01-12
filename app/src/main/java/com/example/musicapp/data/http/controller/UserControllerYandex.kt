package com.example.musicapp.data.http.controller

import com.example.musicapp.data.module.UserYandex
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UserControllerYandex {
    @GET("/info?")
    fun getUser(@Header("Authorization") token: String): Call<UserYandex>
}