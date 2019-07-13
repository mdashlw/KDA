package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.isBoolean

fun Command.Context.nullableBoolean(): Boolean? {
    val arg = take() ?: return null

    return arg.takeIf(String::isBoolean)?.toBoolean()
        ?: error("`$arg` is not a boolean. **(true/false)**")
}

fun Command.Context.boolean(fallback: Boolean? = null): Boolean = nullableBoolean() ?: fallback ?: throw Command.Help()
