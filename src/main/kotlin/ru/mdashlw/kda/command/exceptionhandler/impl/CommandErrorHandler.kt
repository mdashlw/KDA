package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler

object CommandErrorHandler : ExceptionHandler<Command.Error> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Error) {
        event.replyError(exception.message ?: "(no message)").queue()
    }
}
