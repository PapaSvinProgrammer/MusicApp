package com.example.musicapp.app.support

import android.content.Context
import android.util.Log
import com.example.musicapp.data.constant.ErrorConst
import com.example.musicapp.domain.module.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ReadFileFromAssets(private val context: Context) {
    fun readJsonGenre(): List<Genre> {
        var jsonString = ""

        try {
            jsonString = context.assets.open("genre.json")
                .bufferedReader()
                .use {
                    it.readText()
                }
        } catch (e: Exception) {
            Log.e(ErrorConst.ASSETS_ERROR, e.message.toString())
        }

        val type: Type = object: TypeToken<List<Genre>>() {}.type
        val resul: List<Genre> = Gson().fromJson(jsonString, type)

        return resul
    }
}