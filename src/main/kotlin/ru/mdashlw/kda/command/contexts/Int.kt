package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.optionalInt(range: IntRange? = null): Int? {
    val arg = optionalWord() ?: return null
    val number = arg.toIntOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.first.format()}** to **${range.last.format()}**.")
    }

    return number
}

fun Command.Context.int(range: IntRange? = null, fallback: Int? = null): Int =
    optionalInt(range) ?: fallback ?: throw Command.Help()
