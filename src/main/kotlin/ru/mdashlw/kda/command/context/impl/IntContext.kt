package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.util.string.removeNumberFormat
import kotlin.reflect.KParameter

object IntContext : CommandContext<Int>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Int> =
        arg.removeNumberFormat().toIntOrNull()?.map()
            ?: throw Error("Input is not a number.")
}
