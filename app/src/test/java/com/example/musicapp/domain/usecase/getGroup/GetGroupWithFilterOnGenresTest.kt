package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class GetGroupWithFilterOnGenresTest {
    private val repository = mock<GroupRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetGroupWithFilterOnGenres(repository)
        val testFilterUseCase = listOf(1, 2)
        val testResult = listOf(
            Group(
                genre = "FILTER1"
            ),
            Group(
                genre = "FILTER2"
            )
        )

        Mockito.`when`(repository.getGroupWithFilterOnGenre(any())).thenReturn(testResult)

        val expected = listOf(
            Group(
                genre = "FILTER1"
            ),
            Group(
                genre = "FILTER2"
            )
        )
        val actual = useCase.execute(testFilterUseCase)

        Assertions.assertEquals(expected, actual)
    }
}