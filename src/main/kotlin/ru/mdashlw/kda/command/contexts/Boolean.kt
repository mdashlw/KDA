package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.util.isBoolean

fun Command.Context.boolean(fallback: Boolean? = null): Boolean {
    val arg = take() ?: return fallback ?: throw Command.Help()

    return arg.takeIf(String::isBoolean)?.toBoolean()
        ?: error("`$arg` is not a boolean. **(true/false)**")
}
