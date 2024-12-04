package com.example.musicapp.di

import com.example.musicapp.domain.usecase.getGroup.GetGroupAll
import com.example.musicapp.domain.usecase.getGroup.GetGroupWithFilterOnGenres
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import com.example.musicapp.domain.usecase.signAndCreate.CreateAccount
import com.example.musicapp.domain.usecase.signAndCreate.SignInAccount
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetEmail
import com.example.musicapp.domain.usecase.getPreferences.GetLoginState
import com.example.musicapp.domain.usecase.getPreferences.GetUserKey
import com.example.musicapp.domain.usecase.room.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.DeleteMusicFromSQLite
import com.example.musicapp.domain.usecase.room.GetAllMusicFromSQLite
import com.example.musicapp.domain.usecase.saveInternalStorage.SaveInternalStorage
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
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

    factory<GetMusicAll> {
        GetMusicAll(
            musicRepository = get()
        )
    }

    factory<GetGroupAll> {
        GetGroupAll(
            groupRepository = get()
        )
    }

    factory<GetGroupWithFilterOnGenres> {
        GetGroupWithFilterOnGenres(
            groupRepository = get()
        )
    }

    factory<SaveInternalStorage> {
        SaveInternalStorage(
            musicStorageRepository = get()
        )
    }

    factory<AddMusicInSQLite> {
        AddMusicInSQLite(
            musicLiteRepository = get()
        )
    }

    factory<DeleteMusicFromSQLite> {
        DeleteMusicFromSQLite(
            musicLiteRepository = get()
        )
    }

    factory<GetAllMusicFromSQLite> {
        GetAllMusicFromSQLite(
            musicLiteRepository = get()
        )
    }
}