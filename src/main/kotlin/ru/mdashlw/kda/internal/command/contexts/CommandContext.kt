package ru.mdashlw.kda.internal.command.contexts

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.kda.internal.command.findCommand
import kotlin.reflect.KParameter

object CommandContext : Context<Command>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Command> =
        find(text)?.map()
            ?: throw Error(event.localize("contexts.command.unknown", text))

    private fun find(name: String, parent: Command? = null): Command? {
        val command = findCommand(name.substringBefore(' '), parent)

        if (command != null && ' ' in name) {
            return find(name.substringAfter(' '), command)
        }

        return command
    }
}
