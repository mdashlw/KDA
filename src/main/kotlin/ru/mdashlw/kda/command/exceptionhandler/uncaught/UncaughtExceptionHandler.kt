package ru.mdashlw.kda.command.exceptionhandler.uncaught

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler

interface UncaughtExceptionHandler : ExceptionHandler<Throwable> {
    override fun handle(command: Command, event: Command.Event, exception: Throwable)
}
