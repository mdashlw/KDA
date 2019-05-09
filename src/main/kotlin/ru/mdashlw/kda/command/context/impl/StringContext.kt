package ru.mdashlw.kda.command.context.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.annotations.Text
import ru.mdashlw.kda.command.context.CommandContext
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

object StringContext : CommandContext<String> {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): CommandContext.Result<String> =
        if (parameter.findAnnotation<Text>() != null) {
            CommandContext.Result(text, text.count { it == ' ' })
        } else {
            CommandContext.Result(arg)
        }
}
