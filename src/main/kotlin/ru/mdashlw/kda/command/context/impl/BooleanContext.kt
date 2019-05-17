package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.util.string.isBoolean
import kotlin.reflect.KParameter

object BooleanContext : CommandContext<Boolean>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Boolean> =
        arg.takeIf(String::isBoolean)?.toBoolean()?.map()
            ?: throw Error("Input is a not logical type (true/false).")
}
