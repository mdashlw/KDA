package ru.mdashlw.kda.internal.command

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.kda.api.command.ExceptionHandler
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters

internal fun findCommand(name: String, parent: Command? = null): Command? =
    (parent?.subCommands ?: CommandClient.INSTANCE.commands)[name.toLowerCase()]

internal fun Command.findFunction(args: Int): KFunction<Unit>? =
    functions.find { it.valueParameters.size <= args }
        ?: functions.find { it.valueParameters.filterNot(KParameter::isOptional).size <= args }

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> findContext(type: KClass<T>): Context<T>? =
    CommandClient.INSTANCE.contexts[type] as? Context<T>

@Suppress("UNCHECKED_CAST")
internal fun <T : Throwable> findExceptionHandler(type: KClass<T>): ExceptionHandler<T>? =
    CommandClient.INSTANCE.exceptionHandlers[type] as? ExceptionHandler<T>
