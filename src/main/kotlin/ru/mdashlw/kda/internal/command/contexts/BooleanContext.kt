package ru.mdashlw.kda.internal.command.contexts

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.util.isBoolean
import kotlin.reflect.KParameter

object BooleanContext : Context<Boolean>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<Boolean> =
        arg.takeIf(String::isBoolean)?.toBoolean()?.map()
            ?: throw Error(event.localize("contexts.boolean.not_a_boolean"))
}
