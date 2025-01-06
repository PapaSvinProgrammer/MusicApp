package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetGroupAllTest {
    private val repository = mock<GroupRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetGroupAll(repository)
        val testResult = listOf(Group(), Group())

        Mockito.`when`(repository.getGroupAll()).thenReturn(testResult)

        val expected = listOf(Group(), Group())
        val actual = useCase.execute()

        Assertions.assertEquals(expected, actual)
    }
}