package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.kda.command.internal.findCommand
import kotlin.reflect.KParameter

// idk how to name this
object CommandCommandContext : CommandContext<Command> {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): CommandContext.Result<Command> =
        find(text)?.let { CommandContext.Result(it) }
            ?: throw CommandContext.Error("Command does not exist")

    private fun find(name: String, parent: Command? = null): Command? {
        var command = findCommand(name, parent)

        if (command != null && ' ' in name) {
            command = find(name.split(' ')[1], command)
        }

        return command
    }
}
