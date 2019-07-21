package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.util.toBooleanOrNull

fun Command.Context.optionalBoolean(): Boolean? {
    val arg = optionalWord() ?: return null

    return arg.toBooleanOrNull() ?: error("`$arg` is not a boolean. **(true/false)**")
}

fun Command.Context.boolean(fallback: Boolean? = null): Boolean =
    optionalBoolean() ?: fallback ?: throw Command.Help()
