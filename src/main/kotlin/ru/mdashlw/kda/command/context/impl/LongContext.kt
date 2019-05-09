package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.util.string.removeNumberFormat
import kotlin.reflect.KParameter

object LongContext : CommandContext<Long> {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): CommandContext.Result<Long> =
        arg.removeNumberFormat().toLongOrNull()?.let { CommandContext.Result(it) }
            ?: throw CommandContext.Error("Input is not a number")
}
