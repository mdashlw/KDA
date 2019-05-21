package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.kda.command.internal.findCommand
import kotlin.reflect.KParameter

object CommandCommandContext : CommandContext<Command>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Command> =
        find(text)?.map()
            ?: throw Error(event.localize("contexts.command.does_not_exist", text))

    private fun find(name: String, parent: Command? = null): Command? {
        val command = findCommand(name, parent)

        if (command != null && ' ' in name) {
            return find(name.substringAfter(' '), command)
        }

        return command
    }
}
