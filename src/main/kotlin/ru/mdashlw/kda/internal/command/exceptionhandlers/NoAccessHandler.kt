package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler
import ru.mdashlw.kda.api.command.exceptions.NoAccessException

object NoAccessHandler : ExceptionHandler<NoAccessException> {
    override fun handle(command: Command, event: Command.Event, exception: NoAccessException) {
        event.replyError(event.localize("exceptionhandlers.no_access")).queue()
    }
}
