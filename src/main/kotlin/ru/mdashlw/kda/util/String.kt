package ru.mdashlw.kda.util

fun String.toBooleanOrNull(): Boolean? =
    when (toLowerCase()) {
        "true", "yes", "1" -> true
        "false", "no", "0" -> false
        else -> null
    }
