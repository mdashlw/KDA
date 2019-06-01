package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler

object CommandHelpHandler : ExceptionHandler<Command.Help> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Help) {
        event.replyHelp().queue()
    }
}
