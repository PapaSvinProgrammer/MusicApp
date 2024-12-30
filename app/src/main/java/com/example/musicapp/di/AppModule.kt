package com.example.musicapp.di

import com.example.musicapp.presentation.album.AlbumViewModel
import com.example.musicapp.presentation.author.AuthorViewModel
import com.example.musicapp.presentation.playlistItem.PlaylistItemViewModel
import com.example.musicapp.presentation.favorite.FavoriteViewModel
import com.example.musicapp.presentation.home.HomeViewModel
import com.example.musicapp.presentation.login.LoginViewModel
import com.example.musicapp.presentation.main.MainViewModel
import com.example.musicapp.presentation.mainPlayer.PlayerViewModel
import com.example.musicapp.presentation.playlist.PlaylistViewModel
import com.example.musicapp.presentation.playlistFavorite.PlaylistFavoriteViewModel
import com.example.musicapp.presentation.registration.RegistrationViewModel
import com.example.musicapp.presentation.settingPreferences.SettingsPreferencesViewModel
import com.example.musicapp.presentation.settings.SettingsViewModel
import com.example.musicapp.presentation.start.StartViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        LoginViewModel(
            emailValid = get(),
            passwordValid = get(),
            signInAccount = get(),
            saveUserKey = get(),
            saveLoginState = get(),
            saveEmail = get()
        )
    }

    viewModel {
        RegistrationViewModel(
            emailValid = get(),
            passwordValid = get(),
            createAccount = get(),
            saveUserKey = get(),
            saveLoginState = get(),
            saveEmail = get()
        )
    }

    viewModel {
        StartViewModel(
            getLoginState = get(),
            saveDarkModeState = get(),
            getDarkModeState = get()
        )
    }

    viewModel {
        MainViewModel(
            getDarkModeState = get(),
            getUserKey = get(),
            getMusicAll = get(),
            addPlaylistInSQLite = get()
        )
    }

    viewModel {
        SettingsViewModel(
            saveDarkModeState = get(),
            saveLoginState = get(),
            getEmail = get(),
            getDarkModeState = get()
        )
    }

    viewModel {
        SettingsPreferencesViewModel(
            getGroupAll = get(),
            getGroupWithFilterOnGenres = get()
        )
    }

    viewModel {
        HomeViewModel(
            getMusicAll = get(),
            getAlbumsAll = get(),
            getGroupAll = get()
        )
    }

    viewModel {
        PlayerViewModel(
            addMusicInSQLite = get(),
            deleteMusicFromSQLite = get(),
            findFavoriteMusicFromSQLite = get()
        )
    }

    viewModel {
        FavoriteViewModel(
            getMusicFromSQLite = get(),
            getAuthorsFromSQLite = get(),
            convertTextCount = get(),
            getPlaylistFromSQLite = get()
        )
    }

    viewModel {
        PlaylistViewModel(
            getPlaylistFromSQLite = get(),
            addPlaylistInSQLite = get()
        )
    }

    viewModel {
        PlaylistItemViewModel(
            getPlaylistFromSQLite = get(),
            updatePlaylistImage = get(),
            updatePlaylistName = get(),
            deletePlaylistFromSQLite = get()
        )
    }

    viewModel {
        PlaylistFavoriteViewModel(
            getPlaylistFromSQLite = get()
        )
    }

    viewModel {
        AlbumViewModel(
            getAlbumById = get(),
            getMusicsByAlbumId = get()
        )
    }

    viewModel {
        AuthorViewModel(
            getAlbumsByAuthorId = get(),
            getGroupById = get(),
            getMusicsByAuthorId = get()
        )
    }
}