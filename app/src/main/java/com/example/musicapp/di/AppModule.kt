package com.example.musicapp.di

import com.example.musicapp.presintation.playlistItem.PlaylistItemViewModel
import com.example.musicapp.presintation.favorite.FavoriteViewModel
import com.example.musicapp.presintation.home.HomeViewModel
import com.example.musicapp.presintation.login.LoginViewModel
import com.example.musicapp.presintation.main.MainViewModel
import com.example.musicapp.presintation.mainPlayer.PlayerViewModel
import com.example.musicapp.presintation.playlist.PlaylistViewModel
import com.example.musicapp.presintation.registration.RegistrationViewModel
import com.example.musicapp.presintation.settingPreferences.SettingsPreferencesViewModel
import com.example.musicapp.presintation.settings.SettingsViewModel
import com.example.musicapp.presintation.start.StartViewModel
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
            saveLoginState = get()
        )
    }

    viewModel {
        SettingsPreferencesViewModel(
            getGroupAll = get(),
            getGroupWithFilterOnGenres = get()
        )
    }

    viewModel {
        HomeViewModel()
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
            updatePlaylistName = get()
        )
    }
}