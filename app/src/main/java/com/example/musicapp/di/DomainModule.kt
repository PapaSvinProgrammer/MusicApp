package com.example.musicapp.di

import com.example.musicapp.domain.usecase.signAndCreate.CreateAccount
import com.example.musicapp.domain.usecase.signAndCreate.SignInAccount
import com.example.musicapp.domain.usecase.get.GetDarkModeState
import com.example.musicapp.domain.usecase.get.GetEmail
import com.example.musicapp.domain.usecase.get.GetLoginState
import com.example.musicapp.domain.usecase.get.GetUserKey
import com.example.musicapp.domain.usecase.save.SaveDarkModeState
import com.example.musicapp.domain.usecase.save.SaveEmail
import com.example.musicapp.domain.usecase.save.SaveLoginState
import com.example.musicapp.domain.usecase.save.SaveUserKey
import com.example.musicapp.domain.usecase.valid.EmailValid
import com.example.musicapp.domain.usecase.valid.PasswordValid
import org.koin.dsl.module

val domainModule = module {
    factory<EmailValid> {
        EmailValid()
    }

    factory<PasswordValid> {
        PasswordValid()
    }

    factory<SaveLoginState> {
        SaveLoginState(
            sharedPreferencesRepository = get()
        )
    }

    factory<SaveUserKey> {
        SaveUserKey(
            sharedPreferencesRepository = get()
        )
    }

    factory<SaveDarkModeState> {
        SaveDarkModeState(
            sharedPreferencesRepository = get()
        )
    }

    factory<SaveEmail> {
        SaveEmail(
            sharedPreferencesRepository = get()
        )
    }

    factory<GetLoginState> {
        GetLoginState(
            sharedPreferencesRepository = get()
        )
    }

    factory<GetDarkModeState> {
        GetDarkModeState(
            sharedPreferencesRepository = get()
        )
    }

    factory<GetEmail> {
        GetEmail(
            sharedPreferencesRepository = get()
        )
    }

    factory<GetUserKey> {
        GetUserKey(
            sharedPreferencesRepository = get()
        )
    }

    factory<SignInAccount> {
        SignInAccount(
            signAndCreateRepository = get()
        )
    }

    factory<CreateAccount> {
        CreateAccount(
            signAndCreateRepository = get()
        )
    }
}