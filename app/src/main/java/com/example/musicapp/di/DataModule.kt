package com.example.musicapp.di

import android.annotation.SuppressLint
import androidx.room.Room
import com.example.musicapp.data.firebase.getAlbum.GetAlbumFilterImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumByIdImpl
import com.example.musicapp.data.firebase.getAlbum.GetAlbumsAllImpl
import com.example.musicapp.data.firebase.getAnother.GetGroupInfoImpl
import com.example.musicapp.data.firebase.getAnother.GetMusicInfoById
import com.example.musicapp.data.firebase.getGroup.GetGroupAllImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupByIdImpl
import com.example.musicapp.data.firebase.getGroup.GetGroupWithFilterOnGenresImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicAllImpl
import com.example.musicapp.data.firebase.getMusic.GetMusicsByFieldIdImpl
import com.example.musicapp.data.firebase.getAnother.GetMusicTextById
import com.example.musicapp.data.firebase.getMusic.GetMusicsFilterImpl
import com.example.musicapp.data.firebase.getMusic.GetRandomMusicImpl
import com.example.musicapp.data.firebase.search.SearchAlbumImpl
import com.example.musicapp.data.firebase.search.SearchAllImpl
import com.example.musicapp.data.firebase.search.SearchGroupImpl
import com.example.musicapp.data.firebase.search.SearchMusicImpl
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.CreateWithEmailAndPasswordFirebase
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.GetUserGoogleImpl
import com.example.musicapp.data.firebase.signAndCreateWithEmailAndPassword.SignWithEmailAndPasswordFirebase
import com.example.musicapp.data.http.get.GetUserYandexImpl
import com.example.musicapp.data.repository.AlbumRepositoryFirebase
import com.example.musicapp.data.repository.GroupRepositoryFirebase
import com.example.musicapp.data.repository.MusicInfoRepositoryFirebase
import com.example.musicapp.data.repository.MusicRepositoryFirebase
import com.example.musicapp.data.repository.MusicRepositoryRoom
import com.example.musicapp.data.repository.MusicTextRepositoryFirebase
import com.example.musicapp.data.repository.PlaylistRepositoryRoom
import com.example.musicapp.data.repository.SaveMusicRepositoryRoom
import com.example.musicapp.data.repository.SearchRepositoryFirebase
import com.example.musicapp.data.repository.PreferencesRepositoryDataStore
import com.example.musicapp.data.repository.SignAndCreateRepositoryFirebase
import com.example.musicapp.data.repository.UserRepositoryImpl
import com.example.musicapp.data.room.AppDatabase
import com.example.musicapp.data.room.dao.MusicDao
import com.example.musicapp.data.room.dao.PlaylistDao
import com.example.musicapp.data.room.dao.SaveMusicDao
import com.example.musicapp.domain.repository.AlbumRepository
import com.example.musicapp.domain.repository.DownloadMusicRepository
import com.example.musicapp.domain.repository.GroupRepository
import com.example.musicapp.domain.repository.MusicInfoRepository
import com.example.musicapp.domain.repository.MusicLiteRepository
import com.example.musicapp.domain.repository.MusicRepository
import com.example.musicapp.domain.repository.MusicTextRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.SaveMusicRepository
import com.example.musicapp.domain.repository.SearchRepository
import com.example.musicapp.domain.repository.PreferencesRepository
import com.example.musicapp.domain.repository.SignAndCreateRepository
import com.example.musicapp.domain.repository.UserRepository
import com.example.musicapp.app.service.audioDownloader.AudioDownloadHelper
import com.example.musicapp.data.firebase.getMusic.GetMusicsFilterLimitImpl
import com.example.musicapp.presentation.start.GoogleAuthView
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val dataModule = module {
    single<PreferencesRepository> {
        PreferencesRepositoryDataStore(
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
            getMusicsByFieldIdImpl = get(),
            getRandomMusicImpl = get(),
            getMusicsFilterImpl = get(),
            getMusicsFilterLimitImpl = get()
        )
    }

    single<GroupRepository> {
        GroupRepositoryFirebase(
            getGroupAllImpl = get(),
            getGroupWithFilterOnGenresImpl = get(),
            getGroupByIdImpl = get(),
            getGroupInfoImpl = get()
        )
    }

    single<AlbumRepository> {
        AlbumRepositoryFirebase(
            getAlbumByIdImpl = get(),
            getAlbumFilterImpl = get(),
            getAlbumsAllImpl = get()
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

    single<MusicLiteRepository> {
        MusicRepositoryRoom(
            musicDao = get()
        )
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "music_app_favorite_music_database.db"
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

    single<GetAlbumByIdImpl> {
        GetAlbumByIdImpl()
    }

    single<GetMusicsByFieldIdImpl> {
        GetMusicsByFieldIdImpl()
    }

    single<GetGroupByIdImpl> {
        GetGroupByIdImpl()
    }

    single<GetAlbumFilterImpl> {
        GetAlbumFilterImpl()
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

    single<PlaylistRepository> {
        PlaylistRepositoryRoom(
            playlistDao = get()
        )
    }

    single<DownloadMusicRepository> {
        AudioDownloadHelper(
            context = get()
        )
    }

    single<SaveMusicRepository> {
        SaveMusicRepositoryRoom(
            saveMusicDao = get()
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            getUserYandexImpl = get(),
            getUserGoogleImpl = get()
        )
    }

    single<GetUserGoogleImpl> {
        GetUserGoogleImpl()
    }

    single<GetUserYandexImpl> {
        GetUserYandexImpl()
    }

    single<GetRandomMusicImpl> {
        GetRandomMusicImpl()
    }

    single<SearchRepository> {
        SearchRepositoryFirebase(
            searchMusicImpl = get(),
            searchAlbumImpl = get(),
            searchGroupImpl = get(),
            searchAllImpl = get()
        )
    }

    single {
        SearchMusicImpl()
    }

    single {
        SearchAlbumImpl()
    }

    single {
        SearchGroupImpl()
    }

    single {
        SearchAllImpl()
    }

    single {
        GetGroupInfoImpl()
    }

    single {
        GetMusicsFilterImpl()
    }

    single {
        GoogleAuthView()
    }

    single {
        GetMusicsFilterLimitImpl()
    }
}