package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.long(range: LongRange? = null, fallback: Long? = null): Long {
    val arg = take() ?: return fallback ?: throw Command.Help()
    val number = arg.toLongOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.first.format()}** to **${range.last.format()}**.")
    }

    return number
}
