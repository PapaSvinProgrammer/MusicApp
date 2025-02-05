package com.example.musicapp.presintation.album

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.musicapp.domain.module.Album
import com.example.musicapp.domain.usecase.getMusic.GetMusicsByAlbumId
import com.example.musicapp.presentation.album.AlbumViewModel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class AlbumViewModelTest {
    private val getAlbumById = mock<GetAlbumById>()
    private val getMusicsByAlbumId = mock<GetMusicsByAlbumId>()
    private lateinit var viewModel: AlbumViewModel

    @AfterEach
    fun after() {
        Mockito.reset(getAlbumById)
        Mockito.reset(getMusicsByAlbumId)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    @BeforeEach
    fun before() {
        viewModel = AlbumViewModel(
            getAlbumById = getAlbumById,
            getMusicsByAlbumId = getMusicsByAlbumId
        )

        ArchTaskExecutor.getInstance().setDelegate(object: TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }
        })
    }

    @Test
    fun getAlbumTest(): Unit = runBlocking {
        val testId = "albumId"
        val testResult = Album(id = testId)

        Mockito.`when`(getAlbumById.execute(testId)).thenReturn(testResult)
        viewModel.getAlbum(testId)

        val expected = Album(id = testId)
        val actual = viewModel.getAlbumResult.value

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun getMusicsTest(): Unit = runBlocking {

    }
}