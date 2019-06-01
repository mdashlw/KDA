package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.internal.command.CommandClient

interface ExceptionHandler<T : Throwable> {
    fun handle(command: Command, event: Command.Event, exception: T)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Throwable> ExceptionHandler<T>.register() {
    CommandClient.INSTANCE.exceptionHandlers[T::class] = this as ExceptionHandler<Throwable>
}
