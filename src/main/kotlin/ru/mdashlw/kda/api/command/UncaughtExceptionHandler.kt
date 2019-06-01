package ru.mdashlw.kda.api.command

interface UncaughtExceptionHandler : ExceptionHandler<Throwable> {
    override fun handle(command: Command, event: Command.Event, exception: Throwable)
}
