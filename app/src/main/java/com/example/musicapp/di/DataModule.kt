package com.example.musicapp.di

import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPassword
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPassword
import com.example.musicapp.data.repository.GetMusicDataRepositoryImpl
import com.example.musicapp.data.repository.SharedPreferencesRepositoryImpl
import com.example.musicapp.data.repository.SignAndCreateRepositoryImpl
import com.example.musicapp.domain.repository.GetMusicDataRepository
import com.example.musicapp.domain.repository.SharedPreferencesRepository
import com.example.musicapp.domain.repository.SignAndCreateRepository
import org.koin.dsl.module

val dataModule = module {
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(
            context = get()
        )
    }

    single<SignAndCreateRepository> {
        SignAndCreateRepositoryImpl(
            signWithEmailAndPassword = get(),
            createWithEmailAndPassword = get()
        )
    }

    single<SignWithEmailAndPassword> {
        SignWithEmailAndPassword()
    }

    single<CreateWithEmailAndPassword> {
        CreateWithEmailAndPassword()
    }

    single<GetMusicDataRepository> {
        GetMusicDataRepositoryImpl(
            getMusicAllImpl = get(),
            getGroupAllImpl = get(),
            getGroupWithFilterOnGenresImpl = get()
        )
    }

    single<GetMusicAllImpl> {
        GetMusicAllImpl()
    }

    single<GetGroupAllImpl> {
        GetGroupAllImpl()
    }

    single<GetGroupWithFilterOnGenresImpl> {
        GetGroupWithFilterOnGenresImpl()
    }
}