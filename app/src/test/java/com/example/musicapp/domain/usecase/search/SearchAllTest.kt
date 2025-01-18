package com.example.musicapp.domain.usecase.search

import com.example.musicapp.domain.module.Music
import com.example.musicapp.domain.usecase.search.searchFirebase.SearchAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SearchAllTest {
    private val testListName = listOf(
        Music(
            name = "ted"
        ),
        Music(
            name = "testText"
        ),
        Music(
            name = "test"
        ),
        Music(
            name = "aaa"
        )
    )

    private val testListAlbum = listOf(
        Music(
            albumName = "ted"
        ),
        Music(
            albumName = "testText"
        ),
        Music(
            albumName = "test"
        ),
        Music(
            albumName = "aaa"
        )
    )

    private val testListGroup = listOf(
        Music(
           group = "ted"
        ),
        Music(
            group = "testText"
        ),
        Music(
            group = "test"
        ),
        Music(
            group = "aaa"
        )
    )

    private val testListAll = listOf(
        Music(
            name = "nameTed",
            group = "ted",
            albumName = "albumTed"
        ),
        Music(
            name = "nameTestText",
            group = "testText",
            albumName = "albumTestText"
        ),
        Music(
            name = "nameTest",
            group = "test",
            albumName = "albumTest"
        ),
        Music(
            name = "nameAaa",
            group = "aaa",
            albumName = "albumAaa"
        )
    )

    @Test
    fun correctMusicNameSearchExecution() {
        val useCase = SearchAll()
        val testText = "test"

        val expected = listOf(
            Music(
                name = "testText"
            ),
            Music(
                name = "test"
            )
        )
        val actual = useCase.execute(
            text = testText,
            list = testListName
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctMusicAlbumSearchExecution() {
        val useCase = SearchAll()
        val testText = "test"

        val expected = listOf(
            Music(
                albumName = "testText"
            ),
            Music(
                albumName = "test"
            )
        )
        val actual = useCase.execute(
            text = testText,
            list = testListAlbum
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctMusicGroupSearchExecution() {
        val useCase = SearchAll()
        val testText = "test"

        val expected = listOf(
            Music(
                group = "testText"
            ),
            Music(
                group = "test"
            )
        )
        val actual = useCase.execute(
            text = testText,
            list = testListGroup
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctMusicAllSearchExecution() {
        val useCase = SearchAll()
        val testText = "test"

        val expected = listOf(
            Music(
                name = "nameTestText",
                group = "testText",
                albumName = "albumTestText"
            ),
            Music(
                name = "nameTest",
                group = "test",
                albumName = "albumTest"
            )
        )
        val actual = useCase.execute(
            text = testText,
            list = testListAll
        )

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun invalidTextSearchExecution() {
        val useCase = SearchAll()
        val testText1 = "t"
        val testText2 = ""

        val expected = null
        val actual1 = useCase.execute(
            text = testText1,
            list = testListName
        )
        val actual2 = useCase.execute(
            text = testText2,
            list = testListName
        )

        Assertions.assertEquals(expected, actual1)
        Assertions.assertEquals(expected, actual2)
    }

    @Test
    fun invalidListSearchExecution() {
        val useCase = SearchAll()
        val testText = "testText"
        val testList = listOf<Music>()

        val expected = null
        val actual = useCase.execute(
            text = testText,
            list = testList
        )

        Assertions.assertEquals(expected, actual)
    }
}