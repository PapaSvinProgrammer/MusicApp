package com.example.musicapp.domain.usecase.saveInternalStorage

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.repository.MusicStorageRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class SaveInternalStorageTest {
    private val repository = mock<MusicStorageRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturnSingle() {
        val useCase = SaveInternalStorage(repository)
        val testMusic = Music()

        useCase.execute(testMusic)

        Mockito.verify(repository).saveMusic(listOf(testMusic))
    }

    @Test
    fun correctResultReturnList() {
        val useCase = SaveInternalStorage(repository)
        val testMusic = listOf(Music(), Music())

        useCase.execute(testMusic)

        Mockito.verify(repository).saveMusic(listOf(Music(), Music()))
    }
}