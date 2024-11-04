package com.example.musicapp.presintation.main

import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetEmail
import com.example.musicapp.domain.usecase.getPreferences.GetUserKey

class MainViewModel(
    private val getDarkModeState: GetDarkModeState,
    private val getEmail: GetEmail,
    private val getUserKey: GetUserKey
): ViewModel() {
    var darkModeResult: Boolean = false
    var emailResult: String? = null
    var userKeyResult: String? = null

    fun getDarkMode() {
        darkModeResult = getDarkModeState.execute()
    }

    fun getEmail() {
        emailResult = getEmail.execute()
    }

    fun getUserKey() {
        userKeyResult = getUserKey.execute()
    }
}