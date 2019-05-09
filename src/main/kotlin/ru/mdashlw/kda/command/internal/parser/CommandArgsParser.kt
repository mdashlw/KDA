package ru.mdashlw.kda.command.internal.parser

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.kda.command.internal.findCommandContext
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

internal object CommandArgsParser {
    @Suppress("NAME_SHADOWING")
    @Throws(CommandContext.Error::class)
    fun parse(
        event: Command.Event,
        function: KFunction<Unit>,
        args: List<String>
    ): Map<KParameter, Any> {
        var args = args
        val parameters = function.valueParameters
        val parameterCount = parameters.size

        return parameters
            .map {
                val index = parameters.indexOf(it)
                val type = it.type.jvmErasure

                val context = findCommandContext(type)
                    ?: throw NotImplementedError("No command context for $type")

                val result =
                    if (args.isEmpty()) {
                        if (it.isOptional) {
                            return@map null
                        } else {
                            context.resolve(it, event)
                        }
                    } else {
                        context.resolve(
                            it,
                            event,
                            index,
                            args.subList(0, args.size - parameterCount + index + 1).joinToString(" "),
                            args.first()
                        )
                    }

                args = args.drop(result.args)
                it to result.`object`
            }
            .filterNotNull()
            .toMap()
    }
}
