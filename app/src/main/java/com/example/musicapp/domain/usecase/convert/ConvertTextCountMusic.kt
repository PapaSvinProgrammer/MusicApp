package com.example.musicapp.domain.usecase.convert

private const val TEXT_TYPE1 = "трек"
private const val TEXT_TYPE2 = "трека"
private const val TEXT_TYPE3 = "треков"

class ConvertTextCountMusic {
    fun execute(number: Int): String {
        if (number <= 20) {
            return convertSmallNumber(number)
        }
        else {
            return convertBigNumber(number)
        }
    }

    private fun convertSmallNumber(number: Int): String {
        if (number == 1) {
            return TEXT_TYPE1
        }
        else if (number in 2..4) {
            return TEXT_TYPE2
        }

        return TEXT_TYPE3
    }

    private fun convertBigNumber(number: Int): String {
        if (number % 10 == 1) {
            return TEXT_TYPE1
        }
        else if (number % 10 in 2..4) {
            return TEXT_TYPE2
        }

        return TEXT_TYPE3
    }
}