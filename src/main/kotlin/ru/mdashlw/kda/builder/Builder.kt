package ru.mdashlw.kda.builder

abstract class Builder<T> {
    operator fun String?.plus(other: Any?): String =
        this?.plus(other) ?: other?.toString().orEmpty()

    abstract fun build(): T
}
