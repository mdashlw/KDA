package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.byte(range: IntRange? = null, fallback: Byte? = null): Byte {
    val arg = take() ?: return fallback ?: throw Command.Help()
    val number = arg.toByteOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.first.format()}** to **${range.last.format()}**.")
    }

    return number
}
