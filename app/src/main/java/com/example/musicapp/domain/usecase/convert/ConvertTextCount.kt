package com.example.musicapp.domain.usecase.convert

class ConvertTextCount {
    fun execute(number: Int, text: String): String {
        if (number <= 20) {
            return convertSmallNumber(number, text)
        }
        else {
            return convertBigNumber(number, text)
        }
    }

    private fun convertSmallNumber(number: Int, text: String): String {
        if (number == 1) {
            return text
        }
        else if (number in 2..4) {
            return text + "а"
        }

        return text + "ов"
    }

    private fun convertBigNumber(number: Int, text: String): String {
        if (number % 10 == 1) {
            return text
        }
        else if (number % 10 in 2..4) {
            return text + "а"
        }

        return  text + "ов"
    }
}