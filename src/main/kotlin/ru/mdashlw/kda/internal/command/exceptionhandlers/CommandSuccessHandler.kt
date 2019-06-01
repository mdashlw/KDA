package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler

object CommandSuccessHandler : ExceptionHandler<Command.Success> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Success) {
        event.replySuccess(exception.message ?: "(no message)").queue()
    }
}
