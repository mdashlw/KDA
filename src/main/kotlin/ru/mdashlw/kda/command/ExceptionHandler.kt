package ru.mdashlw.kda.command

import ru.mdashlw.kda.command.manager.CommandManager
import kotlin.reflect.KClass

abstract class ExceptionHandler<T : Throwable>(val type: KClass<T>) {
    @Suppress("UNCHECKED_CAST")
    fun register() {
        CommandManager.exceptionHandlers += this as ExceptionHandler<Throwable>
    }

    abstract fun handle(command: Command, context: Command.Context, exception: T)
}
