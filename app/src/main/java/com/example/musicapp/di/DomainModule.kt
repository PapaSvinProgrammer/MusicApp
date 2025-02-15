package com.example.musicapp.di

import com.example.musicapp.app.support.convertTextCount.ConvertAnyText
import com.example.musicapp.app.support.convertTextCount.ConvertTextCount
import com.example.musicapp.app.support.convertTextCount.ConvertTextCountImpl
import com.example.musicapp.domain.usecase.getAlbum.GetAlbum
import com.example.musicapp.domain.usecase.getAlbum.GetAlbumsByAuthorId
import com.example.musicapp.domain.usecase.getAnother.GetGroupInfo
import com.example.musicapp.domain.usecase.getAnother.GetMusicInfo
import com.example.musicapp.domain.usecase.getGroup.GetGroupWithFilterOnGenres
import com.example.musicapp.domain.usecase.getMusic.GetMusicAll
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAlbumId
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAuthorId
import com.example.musicapp.domain.usecase.getAnother.GetMusicText
import com.example.musicapp.domain.usecase.getGroup.GetGroup
import com.example.musicapp.domain.usecase.signAndCreate.CreateAccount
import com.example.musicapp.domain.usecase.signAndCreate.SignInAccount
import com.example.musicapp.domain.usecase.getPreferences.GetDarkModeState
import com.example.musicapp.domain.usecase.getPreferences.GetEmail
import com.example.musicapp.domain.usecase.getPreferences.GetLoginState
import com.example.musicapp.domain.usecase.getPreferences.GetUserKey
import com.example.musicapp.domain.usecase.room.add.AddMusicInSQLite
import com.example.musicapp.domain.usecase.room.add.AddPlaylistInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteMusicFromSQLite
import com.example.musicapp.domain.usecase.room.find.FindMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeletePlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetAuthorsFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetMusicFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetPlaylistFromSQLite
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistImage
import com.example.musicapp.domain.usecase.room.update.UpdatePlaylistName
import com.example.musicapp.domain.usecase.room.delete.DeleteDownloadMusic
import com.example.musicapp.domain.usecase.room.downloadMusic.DownloadMusic
import com.example.musicapp.domain.usecase.room.get.GetCountDownloadMusic
import com.example.musicapp.domain.usecase.room.get.GetDownloadedMusic
import com.example.musicapp.domain.usecase.room.downloadMusic.ManageDownload
import com.example.musicapp.domain.usecase.getMusic.GetRandomMusic
import com.example.musicapp.domain.usecase.http.GetUserGoogle
import com.example.musicapp.domain.usecase.http.GetUserYandex
import com.example.musicapp.domain.usecase.room.add.AddSaveMusicInSQLite
import com.example.musicapp.domain.usecase.room.delete.DeleteSaveMusicFromSQLite
import com.example.musicapp.domain.usecase.room.get.GetCountMusic
import com.example.musicapp.domain.usecase.room.get.GetCountPlaylist
import com.example.musicapp.domain.usecase.room.get.GetMusicsFromPlaylistSQLite
import com.example.musicapp.domain.usecase.room.get.GetTimePlaylist
import com.example.musicapp.domain.usecase.savePreferences.SaveDarkModeState
import com.example.musicapp.domain.usecase.savePreferences.SaveEmail
import com.example.musicapp.domain.usecase.savePreferences.SaveLoginState
import com.example.musicapp.domain.usecase.savePreferences.SaveUserKey
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchAlbum
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchAll
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchGroup
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchMusic
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchDownloadedLocal
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchGroupLocal
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchMusicLocal
import com.example.musicapp.domain.usecase.search.searchSQLite.SearchPlaylistLocal
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
            preferencesRepository = get()
        )
    }

    factory<SaveUserKey> {
        SaveUserKey(
            preferencesRepository = get()
        )
    }

    factory<SaveDarkModeState> {
        SaveDarkModeState(
            preferencesRepository = get()
        )
    }

    factory<SaveEmail> {
        SaveEmail(
            preferencesRepository = get()
        )
    }

    factory<GetLoginState> {
        GetLoginState(
            preferencesRepository = get()
        )
    }

    factory<GetDarkModeState> {
        GetDarkModeState(
            preferencesRepository = get()
        )
    }

    factory<GetEmail> {
        GetEmail(
            preferencesRepository = get()
        )
    }

    factory<GetUserKey> {
        GetUserKey(
            preferencesRepository = get()
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

    factory<GetGroupWithFilterOnGenres> {
        GetGroupWithFilterOnGenres(
            groupRepository = get()
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

    factory<GetMusicFromSQLite> {
        GetMusicFromSQLite(
            musicLiteRepository = get()
        )
    }

    factory<GetAuthorsFromSQLite> {
        GetAuthorsFromSQLite(
            musicLiteRepository = get()
        )
    }

    factory<FindMusicInSQLite> {
        FindMusicInSQLite(
            musicLiteRepository = get()
        )
    }

    factory<ConvertTextCount> {
        ConvertTextCountImpl(
            ConvertAnyText()
        )
    }

    factory<ConvertAnyText> {
        ConvertAnyText()
    }

    factory<GetPlaylistFromSQLite> {
        GetPlaylistFromSQLite(
            playlistRepository = get()
        )
    }

    factory<UpdatePlaylistName> {
        UpdatePlaylistName(
            playlistRepository = get()
        )
    }

    factory<UpdatePlaylistImage> {
        UpdatePlaylistImage(
            playlistRepository = get()
        )
    }

    factory<DeletePlaylistFromSQLite> {
        DeletePlaylistFromSQLite(
            playlistRepository = get()
        )
    }

    factory<GetMusicsByAlbumId> {
        GetMusicsByAlbumId(
            musicRepository = get()
        )
    }

    factory<GetAlbumsByAuthorId> {
        GetAlbumsByAuthorId(
            albumRepository = get()
        )
    }

    factory {
        GetMusicsByAuthorId(
            musicRepository = get()
        )
    }

    factory {
        GetMusicText(
            musicTextRepository = get()
        )
    }

    factory {
        GetMusicInfo(
            musicInfoRepository = get()
        )
    }

    factory {
        SearchMusic(
            searchRepository = get()
        )
    }

    factory {
        AddPlaylistInSQLite(
            playlistRepository = get()
        )
    }

    factory {
        DownloadMusic(
            downloadMusicRepository = get()
        )
    }

    factory {
        DeleteDownloadMusic(
            downloadMusicRepository = get()
        )
    }

    factory {
        GetDownloadedMusic(
            downloadMusicRepository = get()
        )
    }

    factory {
        ManageDownload(
            downloadMusicRepository = get()
        )
    }

    factory {
        AddSaveMusicInSQLite(
            saveMusicRepository = get()
        )
    }

    factory {
        DeleteSaveMusicFromSQLite(
            saveMusicRepository = get()
        )
    }

    factory {
        GetCountMusic(
            playlistRepository = get()
        )
    }

    factory {
        GetCountPlaylist(
            playlistRepository = get()
        )
    }

    factory {
        GetCountDownloadMusic(
            downloadMusicRepository = get()
        )
    }

    factory {
        GetUserYandex(
            userRepository = get()
        )
    }

    factory {
        GetUserGoogle(
            userRepository = get()
        )
    }

    factory {
        GetRandomMusic(
            musicRepository = get()
        )
    }

    factory {
        GetTimePlaylist(
            playlistRepository = get()
        )
    }

    factory {
        SearchAlbum(
            searchRepository = get()
        )
    }

    factory {
        SearchGroup(
            searchRepository = get()
        )
    }

    factory {
        SearchAll(
            searchRepository = get()
        )
    }

    factory {
        SearchPlaylistLocal(
            playlistRepository = get()
        )
    }

    factory {
        SearchMusicLocal(
            musicLiteRepository = get()
        )
    }

    factory {
        SearchDownloadedLocal(
            downloadMusicRepository = get()
        )
    }

    factory {
        SearchGroupLocal(
            musicLiteRepository = get()
        )
    }

    factory {
        GetGroupInfo(
            groupRepository = get()
        )
    }

    factory {
        GetMusicsFromPlaylistSQLite(
            playlistRepository = get()
        )
    }

    factory {
        GetAlbum(
            albumRepository = get()
        )
    }

    factory {
        GetGroup(
            groupRepository = get()
        )
    }
}