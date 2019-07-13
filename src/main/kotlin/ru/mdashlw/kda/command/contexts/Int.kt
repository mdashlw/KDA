package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.nullableInt(range: IntRange? = null): Int? {
    val arg = take() ?: return null
    val number = arg.toIntOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.first.format()}** to **${range.last.format()}**.")
    }

    return number
}

fun Command.Context.int(range: IntRange? = null, fallback: Int? = null): Int =
    nullableInt(range) ?: fallback ?: throw Command.Help()
