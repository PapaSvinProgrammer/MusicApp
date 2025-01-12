package com.example.musicapp.data.http.get

import com.example.musicapp.data.http.controller.UserControllerYandex
import com.example.musicapp.data.module.UserYandex
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://login.yandex.ru"

class GetUserYandexImpl {
    suspend fun execute(token: String): UserYandex? {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val controller = retrofit.create(UserControllerYandex::class.java)

        val call: Call<UserYandex> = controller.getUser(token)

        val response =  CoroutineScope(Dispatchers.IO).async {
            call.execute()
        }.await()

        return response.body()
    }
}