package ru.mdashlw.kda.command.exceptionhandlers

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.NoAccessException

object NoAccessHandler : ExceptionHandler<NoAccessException>(NoAccessException::class) {
    override fun handle(command: Command, context: Command.Context, exception: NoAccessException) {
        context.replyError("You do not have access to use this command.").queue()
    }
}
