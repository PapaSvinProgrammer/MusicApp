package com.example.musicapp.domain.usecase.getAnother

import com.example.musicapp.domain.module.GroupInfo
import com.example.musicapp.domain.repository.GroupRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.never

class GetGroupInfoTest {
    private val repository = mock<GroupRepository>()

    @AfterEach
    fun after() {
        Mockito.reset(repository)
    }

    @Test
    fun correctGet(): Unit = runBlocking {
        val useCase = GetGroupInfo(repository)
        val testId = "groupId"
        val testResult = GroupInfo(id = testId)

        Mockito.`when`(repository.getGroupInfo(testId)).thenReturn(testResult)

        val expected = GroupInfo(id = testId)
        val actual = useCase.execute(testId)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidGet(): Unit = runBlocking {
        val useCase = GetGroupInfo(repository)
        val testId = ""
        val testResult = null

        Mockito.`when`(repository.getGroupInfo(testId)).thenReturn(testResult)

        val expected = null
        val actual = useCase.execute(testId)

        Mockito.verify(repository, never()).getGroupInfo(testId)
        Assertions.assertEquals(expected, actual)
    }
}