package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.optionalShort(range: IntRange? = null): Short? {
    val arg = optionalWord() ?: return null
    val number = arg.toShortOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.first.format()}** to **${range.last.format()}**.")
    }

    return number
}

fun Command.Context.short(range: IntRange? = null, fallback: Short? = null): Short =
    optionalShort(range) ?: fallback ?: throw Command.Help()
