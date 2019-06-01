package ru.mdashlw.kda.internal.command.contexts

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.kda.api.command.annotations.Text
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

object StringContext : Context<String>() {
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
