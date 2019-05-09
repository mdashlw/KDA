package ru.mdashlw.kda.command.exceptionhandler

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient

interface ExceptionHandler<T : Throwable> {
    fun handle(command: Command, event: Command.Event, exception: T)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Throwable> ExceptionHandler<T>.register() {
    CommandClient.INSTANCE.exceptionHandlers[T::class] = this as ExceptionHandler<Throwable>
}
