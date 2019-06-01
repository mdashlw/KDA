package ru.mdashlw.kda.internal.command.contexts

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.util.removeNumberFormat
import kotlin.reflect.KParameter

object LongContext : Context<Long>() {
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
