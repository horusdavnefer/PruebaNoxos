package com.david.pruebanoxos.utils

import java.text.DecimalFormat

class Utils {



    companion object {
        fun concatString(s1: String, s2: String): String{
            return s1.plus(s2)
        }

        fun convertToDecimal(number: String): String{
            val numberInt = number.toInt()
            val integer = numberInt / 100
            val decimal = numberInt % 100
            val fraction = integer + decimal.toDouble() / 100
            val decimalFormat = DecimalFormat("0.00")
            return decimalFormat.format(fraction)
        }
    }

}