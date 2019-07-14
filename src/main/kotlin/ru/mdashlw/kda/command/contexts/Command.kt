package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.manager.CommandManager

fun Command.Context.command(): Command {
    val initialArg = text()
    var arg = initialArg
    var command: Command? = null

    do {
        command = CommandManager.getCommand(arg.substringBefore(" "), command)
        arg = arg.substringAfter(" ", missingDelimiterValue = "")
    } while (arg.isNotEmpty())

    return command ?: error("Command `$initialArg` does not exist.")
}
