package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.format

fun Command.Context.optionalDouble(range: ClosedFloatingPointRange<Double>? = null): Double? {
    val arg = optionalWord() ?: return null
    val number = arg.toDoubleOrNull() ?: error("`$arg` is not a number.")

    if (range != null && number !in range) {
        error("Number `${number.format()}` must be in range from **${range.start.format()}** to **${range.endInclusive.format()}**.")
    }

    return number
}

fun Command.Context.double(range: ClosedFloatingPointRange<Double>? = null, fallback: Double? = null): Double =
    optionalDouble(range) ?: fallback ?: throw Command.Help()
