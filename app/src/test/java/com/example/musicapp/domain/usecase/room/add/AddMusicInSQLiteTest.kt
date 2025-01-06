package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.data.room.musicEntity.AlbumEntity
import com.example.musicapp.data.room.musicEntity.AuthorEntity
import com.example.musicapp.data.room.musicEntity.MusicEntity
import com.example.musicapp.data.room.musicEntity.MusicResult
import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicLiteRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class AddMusicInSQLiteTest {
    private val repository = mock<MusicLiteRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDataInput(): Unit = runBlocking {
        val useCase = AddMusicInSQLite(repository)
        val music = Music()
        val playlistId = 1L

        val testMusicEntity = MusicEntity(
            id = 0,
            firebaseId = music.id.toString(),
            name = music.name.toString(),
            playlistId = playlistId,
            albumId = music.albumId.toString(),
            authorId = music.groupId.toString(),
            url = music.url.toString(),
            movieUrl = music.movieUrl ?: "",
            time = music.time.toString()
        )
        val testAlbumEntity = AlbumEntity(
            firebaseId = music.albumId.toString(),
            name = music.albumName.toString(),
            imageLow = music.imageLow.toString(),
            imageHigh = music.imageHigh.toString()
        )
        val testAuthorEntity = AuthorEntity(
            id = 0,
            firebaseId = music.groupId.toString(),
            name = music.group.toString(),
            imageUrl = music.imageGroup.toString()
        )

        val testMusicResult = MusicResult(
            musicEntity = testMusicEntity,
            albumEntity = testAlbumEntity,
            authorEntity = testAuthorEntity,
            saveMusicEntity = SaveMusicEntity()
        )

        useCase.execute(music, playlistId)

        Mockito.verify(repository, times(1)).add(testMusicResult)
    }
}