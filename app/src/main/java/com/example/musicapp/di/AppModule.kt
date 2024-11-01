package com.example.musicapp.di

import com.example.musicapp.presintation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        LoginViewModel(
            emailValid = get(),
            passwordValid = get()
        )
    }
}