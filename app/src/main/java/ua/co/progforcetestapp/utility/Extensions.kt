package ua.co.progforcetestapp.utility

import kotlin.math.roundToInt

fun Double.toCelsius() = (this - 273).roundToInt()