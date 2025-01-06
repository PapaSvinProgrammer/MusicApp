package com.example.musicapp.domain.usecase.room.add

import com.example.musicapp.data.room.internalMusic.SaveMusicEntity
import com.example.musicapp.domain.module.SaveMusic
import com.example.musicapp.domain.repository.SaveMusicRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times

class AddSaveMusicInSQLiteTest {
    private val repository = mock<SaveMusicRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctDataInput(): Unit = runBlocking {
        val useCase = AddSaveMusicInSQLite(repository)
        val music = SaveMusic()

        val testData =  SaveMusicEntity(
            id = 0,
            firebaseId = music.id,
            uri = music.uri
        )

        useCase.execute(music)

        Mockito.verify(repository, times(1)).insert(testData)
    }
}