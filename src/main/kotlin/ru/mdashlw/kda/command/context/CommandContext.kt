package ru.mdashlw.kda.command.context

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import kotlin.reflect.KParameter

interface CommandContext<T : Any> {
    @Throws(Error::class)
    fun resolve(parameter: KParameter, event: Command.Event, index: Int, text: String, arg: String): Result<T>

    @Throws(Command.Help::class)
    fun resolve(parameter: KParameter, event: Command.Event): Result<T> = throw Command.Help()

    class Result<T : Any>(val `object`: T, val args: Int = 1)

    class Error(message: String) : Exception(message)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> CommandContext<T>.register() {
    CommandClient.INSTANCE.contexts[T::class] = this as CommandContext<Any>
}
