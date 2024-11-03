package com.example.musicapp.presintation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.get.GetDarkModeState
import com.example.musicapp.domain.usecase.get.GetEmail
import com.example.musicapp.domain.usecase.get.GetUserKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val getDarkModeState: GetDarkModeState,
    private val getEmail: GetEmail,
    private val getUserKey: GetUserKey
): ViewModel() {
    var darkModeResult: Boolean = false
    var emailResult: String? = null
    var userKeyResult: String? = null

    fun getDarkMode() {
        CoroutineScope(Dispatchers.IO).launch {
            darkModeResult = getDarkModeState.execute()
        }
    }

    fun getEmail() {
        CoroutineScope(Dispatchers.IO).launch {
            emailResult = getEmail.execute()
        }
    }

    fun getUserKey() {
        CoroutineScope(Dispatchers.IO).launch {
            userKeyResult = getUserKey.execute()
        }
    }
}