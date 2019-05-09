package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler

object CommandWarningHandler : ExceptionHandler<Command.Warning> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Warning) {
        event.replyWarning(exception.message ?: "(no message)").queue()
    }
}
