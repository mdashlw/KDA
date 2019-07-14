package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.manager.CommandManager

fun Command.Context.command(): Command {
    var arg = take() ?: throw Command.Help()
    var command: Command? = null

    do {
        command = CommandManager.getCommand(arg.substringBefore(" "), command)
        arg = arg.substringAfter(" ")
    } while (" " in arg)

    return command ?: error("Command `$arg` does not exist.")
}
