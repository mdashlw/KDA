package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.nullableFloat(range: ClosedFloatingPointRange<Float>? = null): Float? {
    val arg = take() ?: return null
    val number = arg.toFloatOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.start.format()}** to **${range.endInclusive.format()}**.")
    }

    return number
}

fun Command.Context.float(range: ClosedFloatingPointRange<Float>? = null, fallback: Float? = null): Float =
    nullableFloat(range) ?: fallback ?: throw Command.Help()
