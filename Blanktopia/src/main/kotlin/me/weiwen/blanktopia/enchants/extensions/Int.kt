package me.weiwen.blanktopia.enchants.extensions

import kotlin.Int

fun Int.toRomanNumerals(): String {
    val symbols =
        arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
    val numbers = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
    for (i in numbers.indices) {
        if (this >= numbers[i]) {
            return symbols[i] + (this - numbers[i]).toRomanNumerals()
        }
    }
    return ""
}

