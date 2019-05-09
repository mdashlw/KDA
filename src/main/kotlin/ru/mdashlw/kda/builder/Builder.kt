package ru.mdashlw.kda.builder

abstract class Builder<T> {
    // Don't represent neither this nor other as string "null"
    operator fun String?.plus(other: Any?): String =
        this?.plus(other) ?: other?.toString().orEmpty()

    abstract fun build(): T
}
