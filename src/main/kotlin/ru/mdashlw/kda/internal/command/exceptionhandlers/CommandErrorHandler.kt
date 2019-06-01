package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler

object CommandErrorHandler : ExceptionHandler<Command.Error> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Error) {
        event.replyError(exception.message ?: "(no message)").queue()
    }
}
