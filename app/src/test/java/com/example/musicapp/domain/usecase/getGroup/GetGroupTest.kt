package com.example.musicapp.domain.usecase.getGroup

import com.example.musicapp.domain.module.Group
import com.example.musicapp.domain.repository.GroupRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times

class GetGroupTest {
    private val repository = mock<GroupRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGetGroupAll(): Unit = runBlocking {
        val useCase = GetGroup(repository)
        val testResult = listOf(Group(), Group())

        Mockito.`when`(repository.getGroupAll()).thenReturn(testResult)

        val expected = listOf(Group(), Group())
        val actual = useCase.getGroupAll()

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getGroupAll()
    }

    @Test
    fun correctGetGroupById(): Unit = runBlocking {
        val useCase = GetGroup(repository)
        val testId = "testId"
        val testResult = Group(id = testId)

        Mockito.`when`(repository.getGroupById(testId)).thenReturn(testResult)

        val expected = Group(id = testId)
        val actual = useCase.getGroupById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, times(1)).getGroupById(testId)
    }

    @Test
    fun invalidGetGroupById(): Unit = runBlocking {
        val useCase = GetGroup(repository)
        val testId = ""
        val testResult = Group(id = testId)

        Mockito.`when`(repository.getGroupById(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.getGroupById(testId)

        Assertions.assertEquals(expected, actual)
        Mockito.verify(repository, never()).getGroupById(testId)
    }
}