package com.example.musicapp.domain.usecase.convert

import com.example.musicapp.app.support.convertTextCount.ConvertAnyText
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ConvertTextCountImplTest {
    /*
    * Tests for numbers in the range from 0 to 20.
    * For the keyword "трек".
    * For the rest of the keywords,
    * the results will be similar.
    */
    @Test
    fun correctTextConvertUntil20_1() {
        val useCase = ConvertAnyText()
        val testNumber = 1
        val testText = " трек"

        val expected = "1 трек"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertUntil20_2() {
        val useCase = ConvertAnyText()
        val testNumber = 2
        val testText = " трек"

        val expected = "2 трека"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertUntil20_4() {
        val useCase = ConvertAnyText()
        val testNumber = 4
        val testText = " трек"

        val expected = "4 трека"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertUntil20_11() {
        val useCase = ConvertAnyText()
        val testNumber = 11
        val testText = " трек"

        val expected = "11 треков"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertUntil20_20() {
        val useCase = ConvertAnyText()
        val testNumber = 20
        val testText = " трек"

        val expected = "20 треков"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    /*
    * Tests for numbers in the range from 20.
    * For the keyword "альбом".
    * For the rest of the keywords,
    * the results will be similar.
    */
    @Test
    fun correctTextConvertFrom20_21() {
        val useCase = ConvertAnyText()
        val testNumber = 21
        val testText = " альбом"

        val expected = "21 альбом"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertFrom20_222() {
        val useCase = ConvertAnyText()
        val testNumber = 222
        val testText = " альбом"

        val expected = "222 альбома"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertFrom20_45() {
        val useCase = ConvertAnyText()
        val testNumber = 45
        val testText = " альбом"

        val expected = "45 альбомов"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertFrom20_5431() {
        val useCase = ConvertAnyText()
        val testNumber = 5431
        val testText = " альбом"

        val expected = "5431 альбом"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun correctTextConvertFrom20_29() {
        val useCase = ConvertAnyText()
        val testNumber = 29
        val testText = " альбом"

        val expected = "29 альбомов"
        val actual = useCase.execute(testNumber, testText)

        Assertions.assertEquals(expected, actual)
    }
}