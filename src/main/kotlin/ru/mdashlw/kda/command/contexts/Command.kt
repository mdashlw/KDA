package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.manager.CommandManager

fun Command.Context.command(): Command {
    val arg = take() ?: throw Command.Help()

    return CommandManager.getCommandByQualifiedName(arg)
        ?: error("Command `$arg` does not exist.")
}
