package ru.mdashlw.kda.internal.command.contexts

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.util.removeNumberFormat
import kotlin.reflect.KParameter

object IntContext : Context<Int>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Int> =
        arg.removeNumberFormat().toIntOrNull()?.map()
            ?: throw Error(event.localize("contexts.int.not_a_number"))
}
