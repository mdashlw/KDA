package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.internal.command.CommandClient
import kotlin.reflect.KParameter

abstract class Context<T : Any> {
    @Throws(Error::class)
    abstract fun resolve(parameter: KParameter, event: Command.Event, index: Int, text: String, arg: String): Result<T>

    @Throws(Command.Help::class)
    open fun resolve(parameter: KParameter, event: Command.Event): Result<T> = throw Command.Help()

    class Result<T : Any>(val `object`: T, val args: Int = 1)

    class Error(message: String) : Exception(message)

    @Suppress("NOTHING_TO_INLINE")
    inline fun <T : Any> T.map(args: Int = 1): Result<T> =
        Result(this, args)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Context<T>.register() {
    CommandClient.INSTANCE.contexts[T::class] = this as Context<Any>
}
