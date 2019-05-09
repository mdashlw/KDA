package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.NoAccessException

object NoAccessHandler : ExceptionHandler<NoAccessException> {
    override fun handle(command: Command, event: Command.Event, exception: NoAccessException) {
        event.replyError("You do not have access to use this command").queue()
    }
}
