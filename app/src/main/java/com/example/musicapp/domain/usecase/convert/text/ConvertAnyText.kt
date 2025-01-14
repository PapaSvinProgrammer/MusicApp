package com.example.musicapp.domain.usecase.convert.text

class ConvertAnyText {
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
            return number.toString() + text
        }
        else if (number in 2..4) {
            return number.toString() + text + "а"
        }

        return number.toString() + text + "ов"
    }

    private fun convertBigNumber(number: Int, text: String): String {
        if (number % 10 == 1) {
            return number.toString() + text
        }
        else if (number % 10 in 2..4) {
            return number.toString() + text + "а"
        }

        return  number.toString() + text + "ов"
    }
}