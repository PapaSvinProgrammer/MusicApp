package com.example.musicapp.di

import androidx.room.Room
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByFieldIdImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByIdImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumsAllImpl
import com.example.musicapp.data.firebase.getAnother.GetMusicInfoById
import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupByIdImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicsByFieldIdImpl
import com.example.musicapp.data.firebase.getAnother.GetMusicTextById
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPasswordFirebase
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPasswordFirebase
import com.example.musicapp.data.internalStorage.SaveInternalStorageImpl
import com.example.musicapp.data.repository.AlbumRepositoryFirebase
import com.example.musicapp.data.repository.GroupRepositoryFirebase
import com.example.musicapp.data.repository.MusicInfoRepositoryFirebase
import com.example.musicapp.data.repository.MusicInternalStorageRepository
import com.example.musicapp.data.repository.MusicRepositoryFirebase
import com.example.musicapp.data.repository.MusicSQLiteRepository
import com.example.musicapp.data.repository.MusicTextRepositoryFirebase
import com.example.musicapp.data.repository.PlaylistSQLiteRepository
import com.example.musicapp.data.repository.SaveMusicInternalRepository
import com.example.musicapp.data.repository.SharedPreferencesRepositoryImpl
import com.example.musicapp.data.repository.SignAndCreateRepositoryFirebase
import com.example.musicapp.data.room.AppDatabase
import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.dao.SaveMusicDao
import com.example.musicapp.domain.repository.AlbumRepository
import com.example.musicapp.domain.repository.GroupRepository
import com.example.musicapp.domain.repository.MusicInfoRepository
import com.example.musicapp.domain.repository.MusicLiteRepository
import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.repository.MusicStorageRepository
import com.example.musicapp.domain.repository.MusicTextRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.SaveMusicRepository
import com.example.musicapp.domain.repository.SharedPreferencesRepository
import com.example.musicapp.domain.repository.SignAndCreateRepository
import com.example.musicapp.domain.usecase.getAnother.GetMusicInfo
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
            getMusicAllImpl = get(),
            getMusicsByFieldIdImpl = get()
        )
    }

    single<GroupRepository> {
        GroupRepositoryFirebase(
            getGroupAllImpl = get(),
            getGroupWithFilterOnGenresImpl = get(),
            getGroupByIdImpl = get()
        )
    }

    single<AlbumRepository> {
        AlbumRepositoryFirebase(
            getAlbumByIdImpl = get(),
            getAlbumByFieldIdImpl = get(),
            getAlbumsAllImpl = get()
        )
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

    single<MusicLiteRepository> {
        MusicSQLiteRepository(
            musicDao = get()
        )
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            get<SharedPreferencesRepository>().getUserKey()
        ).build()
    }

    single<MusicDao> {
        get<AppDatabase>().getMusicDao()
    }

    single<PlaylistDao> {
        get<AppDatabase>().getPlaylistDao()
    }

    single<SaveMusicDao> {
        get<AppDatabase>().getSaveMusicDao()
    }

    single<PlaylistRepository> {
        PlaylistSQLiteRepository(
            playlistDao = get()
        )
    }

    single<GetAlbumByIdImpl> {
        GetAlbumByIdImpl()
    }

    single<GetMusicsByFieldIdImpl> {
        GetMusicsByFieldIdImpl()
    }

    single<GetGroupByIdImpl> {
        GetGroupByIdImpl()
    }

    single<GetAlbumByFieldIdImpl> {
        GetAlbumByFieldIdImpl()
    }

    single<GetAlbumsAllImpl> {
        GetAlbumsAllImpl()
    }

    single<MusicTextRepository> {
        MusicTextRepositoryFirebase(
            getMusicTextById = get()
        )
    }

    single<GetMusicTextById> {
        GetMusicTextById()
    }

    single<MusicInfoRepository> {
        MusicInfoRepositoryFirebase(
            getMusicInfoById = get()
        )
    }

    single<GetMusicInfoById> {
        GetMusicInfoById()
    }

    single<SaveMusicRepository> {
        SaveMusicInternalRepository(
            saveMusicDao = get()
        )
    }
}