package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.util.removeNumberFormat
import kotlin.reflect.KParameter

object LongContext : CommandContext<Long>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Long> =
        arg.removeNumberFormat().toLongOrNull()?.map()
            ?: throw Error(event.localize("contexts.long.not_a_number"))
}
