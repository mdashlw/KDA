package ru.mdashlw.kda.command.exceptionhandlers

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.ExceptionHandler

object CommandErrorHandler : ExceptionHandler<Command.Error>(Command.Error::class) {
    override fun handle(command: Command, context: Command.Context, exception: Command.Error) {
        context.replyError(exception.message).queue()
    }
}
