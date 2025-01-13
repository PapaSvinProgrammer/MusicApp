package com.example.musicapp.presentation.start

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Controller {
    @GET("/oauth2/public_info")
    fun getUser(
        @Query("client_id") clientId: String,
        @Query("access_token") token: String
    ): Call<UserVk>
}