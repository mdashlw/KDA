package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler

object CommandWarningHandler : ExceptionHandler<Command.Warning> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Warning) {
        event.replyWarning(exception.message ?: "(no message)").queue()
    }
}
