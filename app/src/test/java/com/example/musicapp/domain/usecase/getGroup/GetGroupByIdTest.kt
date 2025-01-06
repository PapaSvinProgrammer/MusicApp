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
import org.mockito.kotlin.never

class GetGroupByIdTest {
    private val repository = mock<GroupRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctResultReturn(): Unit = runBlocking {
        val useCase = GetGroupById(repository)
        val testId = "testId"
        val testResult = Group(
            id = testId
        )

        Mockito.`when`(repository.getGroupById(testId)).thenReturn(testResult)

        val expected = Group(
            id = testId
        )
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultReturn(): Unit = runBlocking {
        val useCase = GetGroupById(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getGroupById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidResultNeverStart(): Unit = runBlocking {
        val useCase = GetGroupById(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getGroupById(testId)).thenReturn(testResult)
        useCase.execute(testId)

        Mockito.verify(repository, never()).getGroupById(any())
    }
}