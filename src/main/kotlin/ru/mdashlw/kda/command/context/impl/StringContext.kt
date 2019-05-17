package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.annotations.Text
import ru.mdashlw.kda.command.context.CommandContext
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

object StringContext : CommandContext<String>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<String> =
        if (parameter.findAnnotation<Text>() != null) {
            text.map(text.count { it == ' ' })
        } else {
            arg.map()
        }
}
