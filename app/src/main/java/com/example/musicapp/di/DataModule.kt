package com.example.musicapp.di

import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPasswordFirebase
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPasswordFirebase
import com.example.musicapp.data.internalStorage.SaveInternalStorageImpl
import com.example.musicapp.data.repository.AlbumRepositoryFirebase
import com.example.musicapp.data.repository.GroupRepositoryFirebase
import com.example.musicapp.data.repository.MusicInternalStorageRepository
import com.example.musicapp.data.repository.MusicRepositoryFirebase
import com.example.musicapp.data.repository.SharedPreferencesRepositoryImpl
import com.example.musicapp.data.repository.SignAndCreateRepositoryFirebase
import com.example.musicapp.domain.repository.AlbumRepository
import com.example.musicapp.domain.repository.GroupRepository
import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.repository.MusicStorageRepository
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
        SignAndCreateRepositoryFirebase(
            signWithEmailAndPasswordFirebase = get(),
            createWithEmailAndPasswordFirebase = get()
        )
    }

    single<SignWithEmailAndPasswordFirebase> {
        SignWithEmailAndPasswordFirebase()
    }

    single<CreateWithEmailAndPasswordFirebase> {
        CreateWithEmailAndPasswordFirebase()
    }

    single<MusicRepository> {
        MusicRepositoryFirebase(
            getMusicAllImpl = get()
        )
    }

    single<GroupRepository> {
        GroupRepositoryFirebase(
            getGroupAllImpl = get(),
            getGroupWithFilterOnGenresImpl = get()
        )
    }

    single<AlbumRepository> {
        AlbumRepositoryFirebase()
    }

    single<MusicStorageRepository> {
        MusicInternalStorageRepository(
            saveInternalStorageImpl = get()
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

    single<SaveInternalStorageImpl> {
        SaveInternalStorageImpl(
            context = get()
        )
    }
}