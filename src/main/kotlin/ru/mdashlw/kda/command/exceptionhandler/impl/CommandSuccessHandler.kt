package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler

object CommandSuccessHandler : ExceptionHandler<Command.Success> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Success) {
        event.replySuccess(exception.message ?: "(no message)").queue()
    }
}
