package com.example.musicapp.di

import com.example.musicapp.presentation.album.AlbumViewModel
import com.example.musicapp.presentation.author.AuthorViewModel
import com.example.musicapp.presentation.authorAlbumList.AlbumListViewModel
import com.example.musicapp.presentation.authorMusicList.MusicListViewModel
import com.example.musicapp.presentation.bottomSheetAddMusic.AddMusicViewModel
import com.example.musicapp.presentation.bottomSheetAuthorInfo.AuthorInfoViewModel
import com.example.musicapp.presentation.playlistItem.PlaylistItemViewModel
import com.example.musicapp.presentation.favorite.FavoriteViewModel
import com.example.musicapp.presentation.home.HomeViewModel
import com.example.musicapp.presentation.login.LoginViewModel
import com.example.musicapp.presentation.main.MainViewModel
import com.example.musicapp.presentation.mainPlayer.PlayerViewModel
import com.example.musicapp.presentation.bottomSheetMusic.MusicBottomSheetViewModel
import com.example.musicapp.presentation.bottomSheetMusicInfo.MusicInfoBottomSheetViewModel
import com.example.musicapp.presentation.bottomSheetMusicText.MusicTextBottomSheetViewModel
import com.example.musicapp.presentation.bottomSheetPlaylistFavorite.PlaylistFavoriteBottomSheetViewModel
import com.example.musicapp.presentation.bottomSheetReport.ReportBottomSheetViewModel
import com.example.musicapp.presentation.download.DownloadViewModel
import com.example.musicapp.presentation.downloadList.DownloadListViewModel
import com.example.musicapp.presentation.playlist.PlaylistViewModel
import com.example.musicapp.presentation.playlistFavorite.PlaylistFavoriteViewModel
import com.example.musicapp.presentation.playlistFavoriteAuthorList.FavoriteAuthorListViewModel
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
            getDarkModeState = get(),
            saveEmail = get(),
            saveUserKey = get(),
            saveLoginState = get(),
            getUserGoogle = get(),
            getUserYandex = get()
        )
    }

    viewModel {
        MainViewModel(
            getDarkModeState = get(),
            addMusicInSQLite = get(),
            deleteMusicFromSQLite = get(),
            getRandomMusic = get(),
            findMusicInSQLite = get()
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
            searchMusic = get(),
            addPlaylistInSQLite = get(),
            searchAlbum = get(),
            searchGroup = get(),
            searchAll = get(),
            getRandomMusic = get()
        )
    }

    viewModel {
        PlayerViewModel(
            addMusicInSQLite = get(),
            deleteMusicFromSQLite = get(),
            findMusicInSQLite = get()
        )
    }

    viewModel {
        FavoriteViewModel(
            getMusicFromSQLite = get(),
            getAuthorsFromSQLite = get(),
            convertTextCount = get(),
            getPlaylistFromSQLite = get(),
            getDownloadedMusic = get(),
            getCountPlaylist = get(),
            getCountDownloadMusic = get(),
            getCountMusic = get()
        )
    }

    viewModel {
        PlaylistViewModel(
            getPlaylistFromSQLite = get(),
            addPlaylistInSQLite = get(),
            searchPlaylistLocal = get()
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
            getPlaylistFromSQLite = get(),
            getCountMusic = get(),
            convertTextCount = get(),
            getTimePlaylist = get(),
            searchMusicLocal = get()
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

    viewModel {
        MusicBottomSheetViewModel(
            findMusicInSQLite = get(),
            addMusicInSQLite = get(),
            downloadMusic = get(),
            deleteDownloadMusic = get(),
            getDownloadedMusic = get(),
            addSaveMusicInSQLite = get(),
            deleteSaveMusicFromSQLite = get()
        )
    }

    viewModel {
        MusicInfoBottomSheetViewModel(
            getMusicInfo = get()
        )
    }

    viewModel {
        MusicTextBottomSheetViewModel(
            getMusicText = get()
        )
    }

    viewModel {
        DownloadViewModel(
            getDownloadedMusic = get()
        )
    }

    viewModel {
        DownloadListViewModel(
            getDownloadedMusic = get(),
            searchDownloadedLocal = get()
        )
    }

    viewModel {
        ReportBottomSheetViewModel()
    }

    viewModel {
        PlaylistFavoriteBottomSheetViewModel()
    }

    viewModel {
        FavoriteAuthorListViewModel(
            getAuthorsFromSQLite = get(),
            searchGroupLocal = get()
        )
    }

    viewModel {
        AlbumListViewModel(
            getAlbumsByAuthorId = get()
        )
    }

    viewModel {
        MusicListViewModel(
            getMusicsByAuthorId = get()
        )
    }

    viewModel {
        AuthorInfoViewModel(
            getGroupInfo = get()
        )
    }

    viewModel {
        AddMusicViewModel(
            searchMusic = get(),
            addMusicInSQLite = get(),
            deleteMusicFromSQLite = get()
        )
    }
}